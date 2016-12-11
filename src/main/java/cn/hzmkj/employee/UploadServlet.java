package cn.hzmkj.employee;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上传数据API
 */
@WebServlet(value = "/api/upload")
public class UploadServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //保存的文件的路径
        String savePath=getServletContext().getRealPath("/upload/");
        File saveDir=new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdirs();
        }

        DiskFileItemFactory factory= new DiskFileItemFactory();
        //临时文件暂存的路径
        File tmpFile = new File(getServletContext().getRealPath("/WEB-INF/")+"/tmp");
        if(!tmpFile.exists()){
            tmpFile.mkdir();
        }
        //缓存临时文件
        factory.setRepository(tmpFile);
        ServletFileUpload sfu= new ServletFileUpload(factory);
        //文件上传的编码格式
        sfu.setHeaderEncoding("utf-8");
        //上传的文件总内存
        long maxSize = (long)1024*1024*1024*5;
        //单文件的最大内存
        long fileMaxSize = (long)1024*1024*1024*5;
        sfu.setFileSizeMax(maxSize);
        sfu.setSizeMax(fileMaxSize);

        String newFileName = "";
        try{

            List<FileItem> itemlist = sfu.parseRequest(request);
            for(FileItem fileItem:itemlist){
                String fieldName=fileItem.getFieldName();
                if(!fileItem.isFormField()){
                    //文件大小
                    Long size=fileItem.getSize();
                    //文件名字
                    String fileName=fileItem.getName();
                    newFileName= UUID.randomUUID().toString().replaceAll("-","")+fileName.substring(fileName.lastIndexOf("."));
                    System.out.println("文件名："+newFileName+"文件大小："+size);

                    File file=new File(savePath,newFileName);
                    fileItem.write(file);
                }
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }
        readExcel(savePath+"/"+newFileName,0);
        readExcel(savePath+"/"+newFileName,1);
        readExcel(savePath+"/"+newFileName,2);
        readExcel(savePath+"/"+newFileName,3);
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
            for(int i=1;i<=lastRowNum;i++){
                row = sheet.getRow(i);
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
                    System.out.println(cellValue);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  processData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from employee where field11=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("field11"));
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                updateEmp(conn, values);
            } else {
                addEmp(conn, values);
            }
            pss.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addEmp(Connection conn, Map<String, String> values) throws SQLException {
        String sql = "insert into employee(field1,field2,field3,field4,field5,field6,field7,field8,field9,field10," +
                "field11,field12,field13,field14,field15,field16,field17,field18,field19,field20," +
                "field21,field22,field23,field24,field25,field26,field27,field28,field29,field30," +
                "field31,field32,field33,field34,field35,field36,field37,field38,field39,field40," +
                "field41,field42,field43,field44,field45,field46,field47,field48,field49,field50," +
                "field51,field52,field53,field54,field55,field56,field57,field58,field59,field60," +
                "field61,field62,field63,field64,field65,field66,field67,field68,field69,field70," +
                "field71,field72,field73,field74,field75,field76,field77,field78,field79,field80," +
                "field81,field82,field83,field84,field85,field86,field87,field88,field89,field90) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1,values.get("field1"));
        pss.setString(2,values.get("field2"));
        pss.setString(3,values.get("field3"));
        pss.setString(4,values.get("field4"));
        pss.setString(5,values.get("field5"));
        pss.setString(6,values.get("field6"));
        pss.setString(7,values.get("field7"));
        pss.setString(8,values.get("field8"));
        pss.setString(9,values.get("field9"));
        pss.setString(10,values.get("field10"));
        pss.setString(11,values.get("field11"));
        pss.setString(12,values.get("field12"));
        pss.setString(13,values.get("field13"));
        pss.setString(14,values.get("field14"));
        pss.setString(15,values.get("field15"));
        pss.setString(16,values.get("field16"));
        pss.setString(17,values.get("field17"));
        pss.setString(18,values.get("field18"));
        pss.setString(19,values.get("field19"));
        pss.setString(20,values.get("field20"));
        pss.setString(21,values.get("field21"));
        pss.setString(22,values.get("field22"));
        pss.setString(23,values.get("field23"));
        pss.setString(24,values.get("field24"));
        pss.setString(25,values.get("field25"));
        pss.setString(26,values.get("field26"));
        pss.setString(27,values.get("field27"));
        pss.setString(28,values.get("field28"));
        pss.setString(29,values.get("field29"));
        pss.setString(30,values.get("field30"));
        pss.setString(31,values.get("field31"));
        pss.setString(32,values.get("field32"));
        pss.setString(33,values.get("field33"));
        pss.setString(34,values.get("field34"));
        pss.setString(35,values.get("field35"));
        pss.setString(36,values.get("field36"));
        pss.setString(37,values.get("field37"));
        pss.setString(38,values.get("field38"));
        pss.setString(39,values.get("field39"));
        pss.setString(40,values.get("field40"));
        pss.setString(41,values.get("field41"));
        pss.setString(42,values.get("field42"));
        pss.setString(43,values.get("field43"));
        pss.setString(44,values.get("field44"));
        pss.setString(45,values.get("field45"));
        pss.setString(46,values.get("field46"));
        pss.setString(47,values.get("field47"));
        pss.setString(48,values.get("field48"));
        pss.setString(49,values.get("field49"));
        pss.setString(50,values.get("field50"));
        pss.setString(51,values.get("field51"));
        pss.setString(52,values.get("field52"));
        pss.setString(53,values.get("field53"));
        pss.setString(54,values.get("field54"));
        pss.setString(55,values.get("field55"));
        pss.setString(56,values.get("field56"));
        pss.setString(57,values.get("field57"));
        pss.setString(58,values.get("field58"));
        pss.setString(59,values.get("field59"));
        pss.setString(60,values.get("field60"));
        pss.setString(71,values.get("field71"));
        pss.setString(72,values.get("field72"));
        pss.setString(73,values.get("field73"));
        pss.setString(74,values.get("field74"));
        pss.setString(75,values.get("field75"));
        pss.setString(76,values.get("field76"));
        pss.setString(77,values.get("field77"));
        pss.setString(78,values.get("field78"));
        pss.setString(79,values.get("field79"));
        pss.setString(80,values.get("field80"));
        pss.setString(81,values.get("field81"));
        pss.setString(82,values.get("field82"));
        pss.setString(83,values.get("field83"));
        pss.setString(84,values.get("field84"));
        pss.setString(85,values.get("field85"));
        pss.setString(86,values.get("field86"));
        pss.setString(87,values.get("field87"));
        pss.setString(88,values.get("field88"));
        pss.setString(89,values.get("field89"));
        pss.setString(90,values.get("field90"));
        pss.executeUpdate();
        pss.close();
    }

    public void updateEmp(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "update employee set field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=?," +
                "field11=?,field12=?,field13=?,field14=?,field15=?,field16=?,field17=?,field18=?,field19=?,field20=?," +
                "field21=?,field22=?,field23=?,field24=?,field25=?,field26=?,field27=?,field28=?,field29=?,field30=?," +
                "field31=?,field32=?,field33=?,field34=?,field35=?,field36=?,field37=?,field38=?,field39=?,field40=?," +
                "field41=?,field42=?,field43=?,field44=?,field45=?,field46=?,field47=?,field48=?,field49=?,field50=?," +
                "field51=?,field52=?,field53=?,field54=?,field55=?,field56=?,field57=?,field58=?,field59=?,field60=?," +
                "field61=?,field62=?,field63=?,field64=?,field65=?,field66=?,field67=?,field68=?,field69=?,field70=?," +
                "field71=?,field72=?,field73=?,field74=?,field75=?,field76=?,field77=?,field78=?,field79=?,field80=?," +
                "field81=?,field82=?,field83=?,field84=?,field85=?,field86=?,field87=?,field88=?,field89=?,field90=? " +
                "where field11=?";

        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1,values.get("field1"));
        pss.setString(2,values.get("field2"));
        pss.setString(3,values.get("field3"));
        pss.setString(4,values.get("field4"));
        pss.setString(5,values.get("field5"));
        pss.setString(6,values.get("field6"));
        pss.setString(7,values.get("field7"));
        pss.setString(8,values.get("field8"));
        pss.setString(9,values.get("field9"));
        pss.setString(10,values.get("field10"));
        pss.setString(11,values.get("field11"));
        pss.setString(12,values.get("field12"));
        pss.setString(13,values.get("field13"));
        pss.setString(14,values.get("field14"));
        pss.setString(15,values.get("field15"));
        pss.setString(16,values.get("field16"));
        pss.setString(17,values.get("field17"));
        pss.setString(18,values.get("field18"));
        pss.setString(19,values.get("field19"));
        pss.setString(20,values.get("field20"));
        pss.setString(21,values.get("field21"));
        pss.setString(22,values.get("field22"));
        pss.setString(23,values.get("field23"));
        pss.setString(24,values.get("field24"));
        pss.setString(25,values.get("field25"));
        pss.setString(26,values.get("field26"));
        pss.setString(27,values.get("field27"));
        pss.setString(28,values.get("field28"));
        pss.setString(29,values.get("field29"));
        pss.setString(30,values.get("field30"));
        pss.setString(31,values.get("field31"));
        pss.setString(32,values.get("field32"));
        pss.setString(33,values.get("field33"));
        pss.setString(34,values.get("field34"));
        pss.setString(35,values.get("field35"));
        pss.setString(36,values.get("field36"));
        pss.setString(37,values.get("field37"));
        pss.setString(38,values.get("field38"));
        pss.setString(39,values.get("field39"));
        pss.setString(40,values.get("field40"));
        pss.setString(41,values.get("field41"));
        pss.setString(42,values.get("field42"));
        pss.setString(43,values.get("field43"));
        pss.setString(44,values.get("field44"));
        pss.setString(45,values.get("field45"));
        pss.setString(46,values.get("field46"));
        pss.setString(47,values.get("field47"));
        pss.setString(48,values.get("field48"));
        pss.setString(49,values.get("field49"));
        pss.setString(50,values.get("field50"));
        pss.setString(51,values.get("field51"));
        pss.setString(52,values.get("field52"));
        pss.setString(53,values.get("field53"));
        pss.setString(54,values.get("field54"));
        pss.setString(55,values.get("field55"));
        pss.setString(56,values.get("field56"));
        pss.setString(57,values.get("field57"));
        pss.setString(58,values.get("field58"));
        pss.setString(59,values.get("field59"));
        pss.setString(60,values.get("field60"));
        pss.setString(71,values.get("field71"));
        pss.setString(72,values.get("field72"));
        pss.setString(73,values.get("field73"));
        pss.setString(74,values.get("field74"));
        pss.setString(75,values.get("field75"));
        pss.setString(76,values.get("field76"));
        pss.setString(77,values.get("field77"));
        pss.setString(78,values.get("field78"));
        pss.setString(79,values.get("field79"));
        pss.setString(80,values.get("field80"));
        pss.setString(81,values.get("field81"));
        pss.setString(82,values.get("field82"));
        pss.setString(83,values.get("field83"));
        pss.setString(84,values.get("field84"));
        pss.setString(85,values.get("field85"));
        pss.setString(86,values.get("field86"));
        pss.setString(87,values.get("field87"));
        pss.setString(88,values.get("field88"));
        pss.setString(89,values.get("field89"));
        pss.setString(90,values.get("field90"));
        pss.executeUpdate();
        pss.close();
    }
}
