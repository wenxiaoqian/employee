package cn.hzmkj.employee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 导出数据
 */
@WebServlet(value = "/api/export")
public class ExportServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String downloanPath = getServletContext().getRealPath("/upload/export.xls");;
        File downloadFile = new File(downloanPath);

        FileInputStream  fis = new FileInputStream(downloadFile);

        String filename= URLEncoder.encode(downloadFile.getName(),"utf-8"); //解决中文文件名下载后乱码的问题
        byte[] b = new byte[fis.available()];
        fis.read(b);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition","attachment; filename="+filename+"");
        //获取响应报文输出流对象
        ServletOutputStream out =response.getOutputStream();
        //输出
        out.write(b);
        out.flush();
        out.close();
    }

    public void readExcel(String fileName, int index){
        try {
            // 读取文件
            InputStream is = new FileInputStream(fileName);
            // 将文件流解析成 POI 文档
            POIFSFileSystem fs = new POIFSFileSystem(is);
            // 再将 POI 文档解析成 Excel 工作簿
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFRow row = null;
            HSSFCell cell = null;
            // 得到第 1 个工作簿
            HSSFSheet sheet = wb.getSheetAt(index);
            // 得到这一行一共有多少列
            int totalColumns = sheet.getRow(0).getPhysicalNumberOfCells();
            // 得到最后一行的坐标
            Integer lastRowNum = sheet.getLastRowNum();
            System.out.println("lastRowNum => " + lastRowNum);


            //获取头部
            Map<Integer, String> headMap = new HashMap<Integer, String>();
            row = sheet.getRow(0);
            for(int j=0;j<totalColumns;j++){
                cell = row.getCell(j);
                String cellValue = cell.getStringCellValue();
                headMap.put(j,cellValue);
            }
            // 从第 2 行开始读
            List<Map<String,String>> values = new ArrayList<Map<String,String>>();
            for(int i=1;i<=lastRowNum;i++){
                row = sheet.getRow(i);
                Map<String, String> value = new HashMap<String,String>();
                for(int j=0;j<totalColumns;j++){
                    cell = row.getCell(j);
                    String cellValue = "";
                    if(cell == null) {
                        cellValue = "";
                    }else if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }else if (cell.getCellType() == cell.CELL_TYPE_STRING){
                        cellValue = String.valueOf(cell.getStringCellValue());
                    }else{
                        cellValue = String.valueOf(cell.getStringCellValue());
                    }
                    value.put(headMap.get(j), cellValue);
                    System.out.println(cellValue);
                }
                values.add(value);
            }

            Connection conn = DBTool.getConnection();
            for(Map<String, String> map : values){

            }
            conn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}