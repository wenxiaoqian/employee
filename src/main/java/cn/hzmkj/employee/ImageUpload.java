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
import java.io.PrintWriter;
import java.util.List;

/**
 * 头像上传
 * Created by wxq-mac on 17/4/4.
 */
@WebServlet(value = "/api/image/upload")
public class ImageUpload  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //保存的文件的路径
        String savePath=getServletContext().getRealPath("/images/");
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

        PrintWriter pw = response.getWriter();
        String newFileName = "";
        try{
            String uname = "";
            String idcard = "";
            List<FileItem> itemlist = sfu.parseRequest(request);
            for(FileItem fileItem:itemlist){
                String fieldName=fileItem.getFieldName();
                if(fileItem.isFormField()){
                    //文件名字
                    String fileName=fileItem.getName();
                    if("uName".equals(fieldName)){
                        uname = fileItem.getString();
                        uname = new String(uname.getBytes("ISO8859-1"), "UTF-8");
                    }
                    if("idCard".equals(fieldName)){
                        idcard = fileItem.getString();
                    }
                }
            }
            newFileName = uname + idcard + ".jpg";
            for(FileItem fileItem:itemlist){
                String fieldName=fileItem.getFieldName();
                if(!fileItem.isFormField()){
                    File file=new File(savePath,newFileName);
                    fileItem.write(file);
                }
            }
            pw.print("success");
        }catch(Exception e){
            e.printStackTrace();
            pw.print("failure");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
