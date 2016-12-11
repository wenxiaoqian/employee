package cn.hzmkj.employee;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        //获取日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM");
        String dirPath=sdf.format(new Date());
        //保存的文件的路径
        String savePath=getServletContext().getRealPath("/upload/"+dirPath);
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
        try{

            List<FileItem> itemlist=sfu.parseRequest(request);
            for(FileItem fileItem:itemlist){
                String fieldName=fileItem.getFieldName();
                System.out.println("控件 称"+fieldName);
                if(fileItem.isFormField()){
                    String value=fileItem.getString();
                    value=new String(value.getBytes("iso8859-1"),"utf-8");
                    System.out.println("普通内容："+fieldName+"值为："+value);
                }else{
                    //文件大小
                    Long size=fileItem.getSize();
                    //文件名字
                    String fileName=fileItem.getName();
                    String newFileName= UUID.randomUUID().toString().replaceAll("-","")+fileName.substring(fileName.lastIndexOf("."));
                    System.out.println("文件名："+newFileName+"文件大小："+size);

                    File file=new File(savePath,newFileName);
                    fileItem.write(file);
                }
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    public static void readExcel(String fileName){
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx")) {
            isE2007 = true;
        }
        try {
            InputStream input = new FileInputStream(fileName);  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007){
                wb = new XSSFWorkbook(input);
            }else {
                wb = new HSSFWorkbook(input);
            }
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                System.out.println("Row #" + row.getRowNum());  //获得行号从0开始
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.println("Cell #" + cell.getColumnIndex());
                    System.out.println(cell.getStringCellValue());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
