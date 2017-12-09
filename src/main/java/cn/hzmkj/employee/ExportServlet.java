package cn.hzmkj.employee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
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

        String uploadPath = getServletContext().getRealPath("/upload/");
        String downloanPath = uploadPath + "/export.xls";

        //复制模板
        //copyFile(uploadPath + "/template.xls", downloanPath);

        exportEmpExcel(downloanPath, 1);
        exportWorkExcel(downloanPath, 2);
        exportFamilyExcel(downloanPath, 3);
        exportEduExcel(downloanPath, 4);
        exportLendExcel(downloanPath, 5);

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

        downloadFile.delete();
    }

    public void exportWorkExcel(String fileName, int index){
        try {
            // 读取文件
            InputStream is = new FileInputStream(fileName);
            // 将文件流解析成 POI 文档
            POIFSFileSystem fs = new POIFSFileSystem(is);
            // 再将 POI 文档解析成 Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(index, "工作履历");
            String heads = "姓名, 身份证号, 起始时间, 终止时间, 工作单位, 部门, 岗位, 岗位标识, 岗位分类-专业, 岗级, 岗位分类-大类, 岗位分类-中类, 岗位分类-小类, 薪级, 预留1, 预留2, 预留3";
            HSSFRow headRow = sheet.createRow(0);
            for(int i=0;i<heads.split(",").length;i++){
                String headString = heads.split(",")[i];
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(headString);
            }
            List<Map<String, String>> empList = loadEmpData();
            int rowNum = 1;
            for(Map<String, String> rowMap : empList) {
                HSSFRow row = sheet.createRow(rowNum);
                for(int i = 0; i < 17; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(rowMap.get("field"+i));
                }
                rowNum++;
            }
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportFamilyExcel(String fileName, int index){
        try {
            // 读取文件
            InputStream is = new FileInputStream(fileName);
            // 将文件流解析成 POI 文档
            POIFSFileSystem fs = new POIFSFileSystem(is);
            // 再将 POI 文档解析成 Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(index, "家庭成员");
            String heads = "姓名,身份证号,成员,姓名,性别,出生日期,年龄,人员政治面貌, 工作单位, 部门, 职务, 预留1, 预留2, 预留3";
            HSSFRow headRow = sheet.createRow(0);
            for(int i=0;i<heads.split(",").length;i++){
                String headString = heads.split(",")[i];
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(headString);
            }
            List<Map<String, String>> empList = loadFamilyData();
            int rowNum = 1;
            for(Map<String, String> rowMap : empList) {
                HSSFRow row = sheet.createRow(rowNum);
                for(int i = 0; i < 14; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(rowMap.get("field"+i));
                }
                rowNum++;
            }
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportEduExcel(String fileName, int index){
        try {
            // 读取文件
            InputStream is = new FileInputStream(fileName);
            // 将文件流解析成 POI 文档
            POIFSFileSystem fs = new POIFSFileSystem(is);
            // 再将 POI 文档解析成 Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(index, "教育经历");
            String heads = "姓名,身份证号,开始时间,结束时间,教育类型,院校/培训机构,专业类别,专业, 学历, 学位, 教育/培训类型, 是否最高学历标识, 是否就业学历标识, 是否最高学位标识, 是否就业学位标识, 预留1, 预留2, 预留3,";
            HSSFRow headRow = sheet.createRow(0);
            for(int i=0;i<heads.split(",").length;i++){
                String headString = heads.split(",")[i];
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(headString);
            }
            List<Map<String, String>> empList = loadEduData();
            int rowNum = 1;
            for(Map<String, String> rowMap : empList) {
                HSSFRow row = sheet.createRow(rowNum);
                for(int i = 0; i < 18; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(rowMap.get("field"+i));
                }
                rowNum++;
            }
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportLendExcel(String fileName, int index){
        try {
            // 读取文件
            InputStream is = new FileInputStream(fileName);
            // 将文件流解析成 POI 文档
            POIFSFileSystem fs = new POIFSFileSystem(is);
            // 再将 POI 文档解析成 Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(index, "借用借出");
            String heads = "姓名,身份证号,单位,部门,部室/班组,岗位,性质,借出/用单位, 借出/用部门, 借出/用班组, 借出/用岗位, 借出/用时间, 结束时间, 期限, 已借出/用时间, 剩余时间, 是否已返回, 补贴标准, 借出/借用通知, 借出/借用备注, 出生时间, 籍贯, 民族, 性别, 工作时间, 政治面貌, 加入时间, 技术职称, 取得时间, 职务, 职务级别, 任职时间, 岗位, 岗位级别, 身份, 现有学历, 毕业学校, 所学专业, 毕业时间, 工作学历, 毕业学校, 所学专业, 毕业时间, 职业资格等级, 专业技术资格名称, 专家人才类型, 预留1, 预留2, 预留3";
            HSSFRow headRow = sheet.createRow(0);
            for(int i=0;i<heads.split(",").length;i++){
                String headString = heads.split(",")[i];
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(headString);
            }
            List<Map<String, String>> empList = loadLendData();
            int rowNum = 1;
            for(Map<String, String> rowMap : empList) {
                HSSFRow row = sheet.createRow(rowNum);
                for(int i = 0; i < 51; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(rowMap.get("field"+i));
                }
                rowNum++;
            }
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportEmpExcel(String fileName, int index){
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(index, "人员信息");
            String heads = "姓名, 单位, 部门, 班组, 系统编号, 出生日期, 籍贯, 民族, 性别, 工作时间, 政治面貌, 政治面貌加入时间, 身份证号, 专业技术资格名称, 职业资格获得时间, 职务, 职务级别、岗位标识, 任现职务时间, 岗位, 岗级, 个人身份, 全日制学历, 全日制毕业学校, 全日制所学专业, 全日制教育毕业时间, 最高学历, 最高教育毕业学校, 最高教育所学专业, 最高教育毕业时间, 职业资格等级, 专业技术资格名称, 专家人才类型, 集中部署ID, 本地ERPID, 薪级, 岗位分类代码, 岗位分类-大类, 岗位分类-中类, 岗位分类-小类, 岗位分类-专业, 提副科时间, 提正科时间, 提副处时间, 提正处时间, 现岗位标识, 任管理岗时间, 任班组长时间, 任副班组长时间, 在班组内工作年限, 全名支援集体标识, 办公地点, 用工性质, 参加工作年限, 复转军人类别, 转业时间, 进入本单位时间, 进入电力行业时间, 薪资计算时间, 政治面貌, 政治面貌加入时间, 全日制学历, 全日制学位, 全日制毕业学校, 全日制所学专业, 全日制专业类别, 全日制教育开始时间, 全日制教育毕业时间, 在职学历, 在职学位, 在职教育毕业学校, 在职教育所学专业, 在职教育专业分类, 在职教育开始业时间, 在职教育毕业时间, 最高学历, 最高学位, 最高教育毕业学校, 最高教育所学专业, 最高教育专业分类, 最高教育开始业时间, 最高教育毕业时间, 技能鉴定工种, 职业资格等级, 职业资格获得时间, 专业技术资格名称, 专业技术资格系列, 专业技术资格等级, 专业技术资格取得时间, 执业技术资格系列, 执业技术资格等级, 执业技术资格取得时间, 专家人才类型, 专家人才类型-取得时间, 取得时间, 到期时间, 任现岗位时间, 任现级别年限, 英语等级1, 分数1, 英语等级2, 分数2, 英语等级3, 分数3, 预留1, 预留2, 预留3, 预留4, 预留5";
            HSSFRow headRow = sheet.createRow(0);
            for(int i=0;i<heads.split(",").length;i++){
                String headString = heads.split(",")[i];
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(headString);
            }
            List<Map<String, String>> empList = loadEmpData();
            int rowNum = 1;
            for(Map<String, String> rowMap : empList) {
                HSSFRow row = sheet.createRow(rowNum);
                for(int i = 0; i < 107; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(rowMap.get("field"+i));
                }
                rowNum++;
            }
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步人事信息
     * @return
     */
    public List<Map<String, String>> loadEmpData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from employee where deletetime is null order by id";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                ResultSetMetaData meata = pss.getMetaData();
                int cols = meata.getColumnCount();
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 1; i <= cols; i++) {
                    String column = meata.getColumnName(i) ;
                    if(!"id".equals(column)){
                        String value = rss.getString(column);
                        if(StringUtils.isBlank(value)){
                            value = "";
                        }
                        map.put(column, value);
                    }
                }
                dataList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步工作经历信息
     * @return
     */
    public List<Map<String, String>> loadWorkData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from works";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                ResultSetMetaData meata = pss.getMetaData();
                int cols = meata.getColumnCount();
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 1; i <= cols; i++) {
                    String column = meata.getColumnName(i) ;

                    String value = rss.getString(column);
                    if(StringUtils.isBlank(value)){
                        value = "";
                    }
                    map.put(column, value);

                }
                dataList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步家庭信息
     * @return
     */
    public List<Map<String, String>> loadFamilyData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from family";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                ResultSetMetaData meata = pss.getMetaData();
                int cols = meata.getColumnCount();
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 1; i <= cols; i++) {
                    String column = meata.getColumnName(i) ;

                    String value = rss.getString(column);
                    if(StringUtils.isBlank(value)){
                        value = "";
                    }
                    map.put(column, value);

                }
                dataList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步教育信息
     * @return
     */
    public List<Map<String, String>> loadEduData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from educate ";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                ResultSetMetaData meata = pss.getMetaData();
                int cols = meata.getColumnCount();
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 1; i <= cols; i++) {
                    String column = meata.getColumnName(i) ;

                    String value = rss.getString(column);
                    if(StringUtils.isBlank(value)){
                        value = "";
                    }
                    map.put(column, value);

                }
                dataList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步借入借出信息
     * @return
     */
    public List<Map<String, String>> loadLendData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from lend";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                ResultSetMetaData meata = pss.getMetaData();
                int cols = meata.getColumnCount();
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 1; i <= cols; i++) {
                    String column = meata.getColumnName(i) ;

                    String value = rss.getString(column);
                    if(StringUtils.isBlank(value)){
                        value = "";
                    }
                    map.put(column, value);

                }
                dataList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

}