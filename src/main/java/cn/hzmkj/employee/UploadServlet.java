package cn.hzmkj.employee;

import java.io.PrintWriter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        PrintWriter pw = response.getWriter();
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

            readExcel(savePath+"/"+newFileName,0);
            readExcel(savePath+"/"+newFileName,1);
            readExcel(savePath+"/"+newFileName,2);
            readExcel(savePath+"/"+newFileName,3);
            readExcel(savePath+"/"+newFileName,4);
            pw.print("success");
        }catch(Exception e){
            e.printStackTrace();
            pw.print("failure");
        }
        pw.flush();
        pw.close();
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
                if(index == 0) {
                    processEmpData(conn, map);
                }else if(index == 1){
                    processWorksData(conn, map);
                }else if(index == 2){
                    processEducateData(conn, map);
                }else if(index == 3){
                    processFamilyData(conn, map);
                }else if(index == 4){
                    processLendData(conn, map);
                }
            }
            conn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  processEmpData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from employee where field13=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("身份证号"));
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

    public void  processFamilyData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from family where field2=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("身份证号"));
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                updateFamily(conn, values);
            } else {
                addFamily(conn, values);
            }
            pss.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void  processEducateData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from educate where field2=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("身份证号"));
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                updateEducate(conn, values);
            } else {
                addEducate(conn, values);
            }
            pss.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void  processWorksData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from works where field2=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("身份证号"));
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                updateWork(conn, values);
            } else {
                addWork(conn, values);
            }
            pss.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void  processLendData(Connection conn, Map<String, String> values){
        try {
            String sql = "select * from lend where field2=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, values.get("身份证号"));
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                updateLend(conn, values);
            } else {
                addLend(conn, values);
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
                "field81,field82,field83,field84,field85,field86,field87,field88,field89,field90," +
                "field91,field92,field93,field94,field95,field96,field97,field98,field99,field100," +
                "field101,field102,field103,field104,field105,field106,field107,field108,field109,createtime,updatetime) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1,values.get("姓名"));
        pss.setString(2,values.get("单位"));
        pss.setString(3,values.get("部门"));
        pss.setString(4,values.get("班组"));
        pss.setString(5,values.get("系统编号"));
        pss.setString(6,values.get("出生日期"));
        pss.setString(7,values.get("籍贯"));
        pss.setString(8,values.get("民族"));
        pss.setString(9,values.get("性别"));
        pss.setString(10,values.get("工作时间"));
        pss.setString(11,values.get("政治面貌"));
        pss.setString(12,values.get("政治面貌加入时间"));
        pss.setString(13,values.get("身份证号"));
        pss.setString(14,values.get("专业技术资格名称"));
        pss.setString(15,values.get("职业资格获得时间"));
        pss.setString(16,values.get("职务"));
        pss.setString(17,values.get("职务级别、岗位标识"));
        pss.setString(18,values.get("任现职务时间"));
        pss.setString(19,values.get("岗位"));
        if(StringUtils.isBlank(values.get("岗级"))) {
            pss.setInt(20, 0);
        }else{
            pss.setInt(20, Integer.valueOf(values.get("岗级")));
        }
        pss.setString(21,values.get("个人身份"));
        pss.setString(22,values.get("全日制学历"));
        pss.setString(23,values.get("全日制毕业学校"));
        pss.setString(24,values.get("全日制所学专业"));
        pss.setString(25,values.get("全日制教育毕业时间"));
        pss.setString(26,values.get("最高学历"));
        pss.setString(27,values.get("最高教育毕业学校"));
        pss.setString(28,values.get("最高教育所学专业"));
        pss.setString(29,values.get("最高教育毕业时间"));
        pss.setString(30,values.get("职业资格等级"));
        pss.setString(31,values.get("专业技术资格名称"));
        pss.setString(32,values.get("专家人才类型"));
        pss.setString(33,values.get("集中部署ID"));
        pss.setString(34,values.get("本地ERPID"));
        if(StringUtils.isBlank(values.get("薪级"))) {
            pss.setInt(35, 0);
        }else{
            pss.setInt(35, Integer.valueOf(values.get("薪级")));
        }
        pss.setString(36,values.get("岗位分类代码"));
        pss.setString(37,values.get("岗位分类-大类"));
        pss.setString(38,values.get("岗位分类-中类"));
        pss.setString(39,values.get("岗位分类-小类"));
        pss.setString(40,values.get("岗位分类-专业"));
        pss.setString(41,values.get("提副科时间"));
        pss.setString(42,values.get("提正科时间"));
        pss.setString(43,values.get("提副处时间"));
        pss.setString(44,values.get("提正处时间"));
        pss.setString(45,values.get("现岗位标识"));
        pss.setString(46,values.get("任管理岗时间"));
        pss.setString(47,values.get("任班组长时间"));
        pss.setString(48,values.get("任副班组长时间"));
        pss.setString(49,values.get("在班组内工作年限"));
        pss.setString(50,values.get("全名支援集体标识"));
        pss.setString(51,values.get("办公地点"));
        pss.setString(52,values.get("用工性质"));
        pss.setString(53,values.get("参加工作年限"));
        pss.setString(54,values.get("复转军人类别"));
        pss.setString(55,values.get("转业时间"));
        pss.setString(56,values.get("进入本单位时间"));
        pss.setString(57,values.get("进入电力行业时间"));
        pss.setString(58,values.get("薪资计算时间"));
        pss.setString(59,values.get("政治面貌"));
        pss.setString(60,values.get("政治面貌加入时间"));
        pss.setString(61,values.get("全日制学历"));
        pss.setString(62,values.get("全日制学位"));
        pss.setString(63,values.get("全日制毕业学校"));
        pss.setString(64,values.get("全日制所学专业"));
        pss.setString(65,values.get("全日制专业类别"));
        pss.setString(66,values.get("全日制教育开始时间"));
        pss.setString(67,values.get("全日制教育毕业时间"));
        pss.setString(68,values.get("在职学历"));
        pss.setString(69,values.get("在职学位"));
        pss.setString(70,values.get("在职教育毕业学校"));
        pss.setString(71,values.get("在职教育所学专业"));
        pss.setString(72,values.get("在职教育专业分类"));
        pss.setString(73,values.get("在职教育开始业时间"));
        pss.setString(75,values.get("在职教育毕业时间"));
        pss.setString(76,values.get("最高学历"));
        pss.setString(77,values.get("最高学位"));
        pss.setString(78,values.get("最高教育毕业学校"));
        pss.setString(79,values.get("最高教育所学专业"));
        pss.setString(80,values.get("最高教育专业分类"));
        pss.setString(81,values.get("最高教育开始业时间"));
        pss.setString(82,values.get("最高教育毕业时间"));
        pss.setString(83,values.get("技能鉴定工种"));
        pss.setString(84,values.get("职业资格等级"));
        pss.setString(85,values.get("职业资格获得时间"));
        pss.setString(86,values.get("专业技术资格名称"));
        pss.setString(87,values.get("专业技术资格系列"));
        pss.setString(88,values.get("专业技术资格等级"));
        pss.setString(89,values.get("专业技术资格取得时间"));
        pss.setString(90,values.get("执业技术资格系列"));
        pss.setString(91,values.get("执业技术资格等级"));
        pss.setString(92,values.get("执业技术资格取得时间"));
        pss.setString(93,values.get("专家人才类型"));
        pss.setString(94,values.get("专家人才类型-取得时间"));
        pss.setString(95,values.get("取得时间"));
        pss.setString(96,values.get("到期时间"));
        pss.setString(97, values.get("任现岗位时间"));
        pss.setString(98, values.get("任现级别年限"));
        pss.setString(99,values.get("英语等级1"));
        pss.setString(100,values.get("分数1"));
        pss.setString(101,values.get("英语等级2"));
        pss.setString(102,values.get("分数2"));
        pss.setString(103,values.get("英语等级3"));
        pss.setString(104,values.get("分数3"));
        pss.setString(105,values.get("预留1"));
        pss.setString(106,values.get("预留2"));
        pss.setString(107,values.get("预留3"));
        pss.setString(108,values.get("预留4"));
        pss.setString(109,values.get("预留5"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(110,time);
        pss.setString(111,time);
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
                "field81=?,field82=?,field83=?,field84=?,field85=?,field86=?,field87=?,field88=?,field89=?,field90=?," +
                "field91=?,field92=?,field93=?,field94=?,field95=?,field96=?,field97=?,field98=?,field99=?,field100=?," +
                "field101=?,field102=?,field103=?,field104=?,field105=?,field106=?,field107=?,field108=?,field109=?,updatetime=?,deletetime='' " +
                "where field13=?";

        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1,values.get("姓名"));
        pss.setString(2,values.get("单位"));
        pss.setString(3,values.get("部门"));
        pss.setString(4,values.get("班组"));
        pss.setString(5,values.get("系统编号"));
        pss.setString(6,values.get("出生日期"));
        pss.setString(7,values.get("籍贯"));
        pss.setString(8,values.get("民族"));
        pss.setString(9,values.get("性别"));
        pss.setString(10,values.get("工作时间"));
        pss.setString(11,values.get("政治面貌"));
        pss.setString(12,values.get("政治面貌加入时间"));
        pss.setString(13,values.get("身份证号"));
        pss.setString(14,values.get("专业技术资格名称"));
        pss.setString(15,values.get("职业资格获得时间"));
        pss.setString(16,values.get("职务"));
        pss.setString(17,values.get("职务级别、岗位标识"));
        pss.setString(18,values.get("任现职务时间"));
        pss.setString(19,values.get("岗位"));
        if(StringUtils.isBlank(values.get("岗级"))) {
            pss.setInt(20, 0);
        }else{
            pss.setInt(20, Integer.valueOf(values.get("岗级")));
        }
        pss.setString(21,values.get("个人身份"));
        pss.setString(22,values.get("全日制学历"));
        pss.setString(23,values.get("全日制毕业学校"));
        pss.setString(24,values.get("全日制所学专业"));
        pss.setString(25,values.get("全日制教育毕业时间"));
        pss.setString(26,values.get("最高学历"));
        pss.setString(27,values.get("最高教育毕业学校"));
        pss.setString(28,values.get("最高教育所学专业"));
        pss.setString(29,values.get("最高教育毕业时间"));
        pss.setString(30,values.get("职业资格等级"));
        pss.setString(31,values.get("专业技术资格名称"));
        pss.setString(32,values.get("专家人才类型"));
        pss.setString(33,values.get("集中部署ID"));
        pss.setString(34,values.get("本地ERPID"));
        if(StringUtils.isBlank(values.get("薪级"))) {
            pss.setInt(35, 0);
        }else{
            pss.setInt(35, Integer.valueOf(values.get("薪级")));
        }
        pss.setString(36,values.get("岗位分类代码"));
        pss.setString(37,values.get("岗位分类-大类"));
        pss.setString(38,values.get("岗位分类-中类"));
        pss.setString(39,values.get("岗位分类-小类"));
        pss.setString(40,values.get("岗位分类-专业"));
        pss.setString(41,values.get("提副科时间"));
        pss.setString(42,values.get("提正科时间"));
        pss.setString(43,values.get("提副处时间"));
        pss.setString(44,values.get("提正处时间"));
        pss.setString(45,values.get("现岗位标识"));
        pss.setString(46,values.get("任管理岗时间"));
        pss.setString(47,values.get("任班组长时间"));
        pss.setString(48,values.get("任副班组长时间"));
        pss.setString(49,values.get("在班组内工作年限"));
        pss.setString(50,values.get("全名支援集体标识"));
        pss.setString(51,values.get("办公地点"));
        pss.setString(52,values.get("用工性质"));
        pss.setString(53,values.get("参加工作年限"));
        pss.setString(54,values.get("复转军人类别"));
        pss.setString(55,values.get("转业时间"));
        pss.setString(56,values.get("进入本单位时间"));
        pss.setString(57,values.get("进入电力行业时间"));
        pss.setString(58,values.get("薪资计算时间"));
        pss.setString(59,values.get("政治面貌"));
        pss.setString(60,values.get("政治面貌加入时间"));
        pss.setString(61,values.get("全日制学历"));
        pss.setString(62,values.get("全日制学位"));
        pss.setString(63,values.get("全日制毕业学校"));
        pss.setString(64,values.get("全日制所学专业"));
        pss.setString(65,values.get("全日制专业类别"));
        pss.setString(66,values.get("全日制教育开始时间"));
        pss.setString(67,values.get("全日制教育毕业时间"));
        pss.setString(68,values.get("在职学历"));
        pss.setString(69,values.get("在职学位"));
        pss.setString(70,values.get("在职教育毕业学校"));
        pss.setString(71,values.get("在职教育所学专业"));
        pss.setString(72,values.get("在职教育专业分类"));
        pss.setString(73,values.get("在职教育开始业时间"));
        pss.setString(75,values.get("在职教育毕业时间"));
        pss.setString(76,values.get("最高学历"));
        pss.setString(77,values.get("最高学位"));
        pss.setString(78,values.get("最高教育毕业学校"));
        pss.setString(79,values.get("最高教育所学专业"));
        pss.setString(80,values.get("最高教育专业分类"));
        pss.setString(81,values.get("最高教育开始业时间"));
        pss.setString(82,values.get("最高教育毕业时间"));
        pss.setString(83,values.get("技能鉴定工种"));
        pss.setString(84,values.get("职业资格等级"));
        pss.setString(85,values.get("职业资格获得时间"));
        pss.setString(86,values.get("专业技术资格名称"));
        pss.setString(87,values.get("专业技术资格系列"));
        pss.setString(88,values.get("专业技术资格等级"));
        pss.setString(89,values.get("专业技术资格取得时间"));
        pss.setString(90,values.get("执业技术资格系列"));
        pss.setString(91,values.get("执业技术资格等级"));
        pss.setString(92,values.get("执业技术资格取得时间"));
        pss.setString(93,values.get("专家人才类型"));
        pss.setString(94,values.get("专家人才类型-取得时间"));
        pss.setString(95,values.get("取得时间"));
        pss.setString(96,values.get("到期时间"));
        pss.setString(97, values.get("任现岗位时间"));
        pss.setString(98, values.get("任现级别年限"));
        pss.setString(99,values.get("英语等级1"));
        pss.setString(100,values.get("分数1"));
        pss.setString(101,values.get("英语等级2"));
        pss.setString(102,values.get("分数2"));
        pss.setString(103,values.get("英语等级3"));
        pss.setString(104,values.get("分数3"));
        pss.setString(105,values.get("预留1"));
        pss.setString(106,values.get("预留2"));
        pss.setString(107,values.get("预留3"));
        pss.setString(108,values.get("预留4"));
        pss.setString(109,values.get("预留5"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(110,time);
        pss.setString(111,values.get("身份证号"));
        pss.executeUpdate();
        pss.close();
    }

    public void addWork(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "insert into works(id,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10," +
                "field11,field12,field13,field14,field15,field16,field17,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, getUUId());
        pss.setString(2, values.get("姓名"));
        pss.setString(3, values.get("身份证号"));
        pss.setString(4, values.get("起始时间"));
        pss.setString(5, values.get("终止时间"));
        pss.setString(6, values.get("工作单位"));
        pss.setString(7, values.get("部门"));
        pss.setString(8, values.get("岗位"));
        if(StringUtils.isBlank(values.get("岗位标识"))) {
            pss.setInt(9, 0);
        }else{
            pss.setInt(9, Integer.valueOf(values.get("岗位标识")));
        }
        pss.setString(10, values.get("岗位分类-专业"));
        if(StringUtils.isBlank(values.get("岗级"))){
            pss.setInt(11, 0);
        }else {
            pss.setInt(11, Integer.valueOf(values.get("岗级")));
        }
        pss.setString(12, values.get("岗位分类-大类"));
        pss.setString(13, values.get("岗位分类-中类"));
        pss.setString(14, values.get("岗位分类-小类"));
        pss.setString(15, values.get("薪级"));
        pss.setString(16, values.get("预留1"));
        pss.setString(17, values.get("预留2"));
        pss.setString(18, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(19, time);
        pss.setString(20, time);
        pss.executeUpdate();
    }

    public void updateWork(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "update works set field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=?," +
                "field11=?,field12=?,field13=?,field14=?,field15=?,field16=?,field17=?,updatetime=?,deletetime='' where field2=?";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, values.get("姓名"));
        pss.setString(2, values.get("身份证号"));
        pss.setString(3, values.get("起始时间"));
        pss.setString(4, values.get("终止时间"));
        pss.setString(5, values.get("工作单位"));
        pss.setString(6, values.get("部门"));
        pss.setString(7, values.get("岗位"));
        if(StringUtils.isBlank(values.get("岗位标识"))) {
            pss.setInt(8, 0);
        }else{
            pss.setInt(8, Integer.valueOf(values.get("岗位标识")));
        }
        pss.setString(9, values.get("岗位分类-专业"));
        if(StringUtils.isBlank(values.get("岗级"))) {
            pss.setInt(10, 0);
        }else{
            pss.setInt(10, Integer.valueOf(values.get("岗级")));
        }
        pss.setString(11, values.get("岗位分类-大类"));
        pss.setString(12, values.get("岗位分类-中类"));
        pss.setString(13, values.get("岗位分类-小类"));
        pss.setString(14, values.get("薪级"));
        pss.setString(15, values.get("预留1"));
        pss.setString(16, values.get("预留2"));
        pss.setString(17, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(18, time);
        pss.setString(19, values.get("身份证号"));
        pss.executeUpdate();
    }

    public void addFamily(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "insert into family(id,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10," +
                "field11,field12,field13,field14,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, getUUId());
        pss.setString(2, values.get("姓名"));
        pss.setString(3, values.get("身份证号"));
        pss.setString(4, values.get("成员"));
        pss.setString(5, values.get("姓名"));
        pss.setString(6, values.get("性别"));
        pss.setString(7, values.get("出生日期"));
        pss.setString(8, values.get("年龄"));
        pss.setString(9, values.get("人员政治面貌"));
        pss.setString(10, values.get("工作单位"));
        pss.setString(11, values.get("部门"));
        pss.setString(12, values.get("职务"));
        pss.setString(13, values.get("预留1"));
        pss.setString(14, values.get("预留2"));
        pss.setString(15, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(16, time);
        pss.setString(17, time);
        pss.executeUpdate();
    }

    public void updateFamily(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "update family set field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=?," +
                "field11=?,field12=?,field13=?,field14=?,updatetime=? where field2=?,deletetime=''";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, values.get("姓名"));
        pss.setString(2, values.get("身份证号"));
        pss.setString(3, values.get("成员"));
        pss.setString(4, values.get("姓名"));
        pss.setString(5, values.get("性别"));
        pss.setString(6, values.get("出生日期"));
        pss.setString(7, values.get("年龄"));
        pss.setString(8, values.get("人员政治面貌"));
        pss.setString(9, values.get("工作单位"));
        pss.setString(10, values.get("部门"));
        pss.setString(11, values.get("职务"));
        pss.setString(12, values.get("预留1"));
        pss.setString(13, values.get("预留2"));
        pss.setString(14, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(15, time);
        pss.setString(16, values.get("身份证号"));
        pss.executeUpdate();
    }

    public void addEducate(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "insert into educate(id,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10," +
                "field11,field12,field13,field14,field15,field16,field17,field18,createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, getUUId());
        pss.setString(2, values.get("姓名"));
        pss.setString(3, values.get("身份证号"));
        pss.setString(4, values.get("开始时间"));
        pss.setString(5, values.get("结束时间"));
        pss.setString(6, values.get("教育类型"));
        pss.setString(7, values.get("院校/培训机构"));
        pss.setString(8, values.get("专业类别"));
        pss.setString(9, values.get("专业"));
        pss.setString(10, values.get("学历"));
        pss.setString(11, values.get("学位"));
        pss.setString(12, values.get("教育/培训类型"));
        pss.setString(13, values.get("是否最高学历标识"));
        pss.setString(14, values.get("是否就业学历标识"));
        pss.setString(15, values.get("是否最高学位标识"));
        pss.setString(16, values.get("是否就业学位标识"));
        pss.setString(17, values.get("预留1"));
        pss.setString(18, values.get("预留2"));
        pss.setString(19, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(20, time);
        pss.setString(21, time);
        pss.executeUpdate();
    }

    public void updateEducate(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "update educate set field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=?," +
                "field11=?,field12=?,field13=?,field14=?,field15=?,field16=?,field17=?,field18=?,updatetime=?,deletetime='' where field2=?";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, values.get("姓名"));
        pss.setString(2, values.get("身份证号"));
        pss.setString(3, values.get("开始时间"));
        pss.setString(4, values.get("结束时间"));
        pss.setString(5, values.get("教育类型"));
        pss.setString(6, values.get("院校/培训机构"));
        pss.setString(7, values.get("专业类别"));
        pss.setString(8, values.get("专业"));
        pss.setString(9, values.get("学历"));
        pss.setString(10, values.get("学位"));
        pss.setString(11, values.get("教育/培训类型"));
        pss.setString(12, values.get("是否最高学历标识"));
        pss.setString(13, values.get("是否就业学历标识"));
        pss.setString(14, values.get("是否最高学位标识"));
        pss.setString(15, values.get("是否就业学位标识"));
        pss.setString(16, values.get("预留1"));
        pss.setString(17, values.get("预留2"));
        pss.setString(18, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(19, time);
        pss.setString(20, values.get("身份证号"));
        pss.executeUpdate();
    }

    public void addLend(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "insert into lend(id,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10," +
                "field11,field12,field13,field14,field15,field16,field17,field18,field19,field20,"
            + "field21,field22,field23,field24,field25,field26,field27,field28,field29,field30,"
            + "field31,field32,field33,field34,field35,field36,field37,field38,field39,field40,"
            + "field41,field42,field43,field44,field45,field46,field47,field48,field49,createtime,updatetime) "
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, getUUId());
        pss.setString(2, values.get("姓名"));
        pss.setString(3, values.get("身份证号"));
        pss.setString(4, values.get("单位"));
        pss.setString(5, values.get("部门"));
        pss.setString(6, values.get("部室/班组"));
        pss.setString(7, values.get("岗位"));
        pss.setString(8, values.get("性质"));
        pss.setString(9, values.get("借出/用单位"));
        pss.setString(10, values.get("借出/用部门"));
        pss.setString(11, values.get("借出/用班组"));
        pss.setString(12, values.get("借出/用岗位"));
        pss.setString(13, values.get("借出/用时间"));
        pss.setString(14, values.get("结束时间"));
        pss.setString(15, values.get("期限"));
        pss.setString(16, values.get("已借出/用时间"));
        pss.setString(17, values.get("剩余时间"));
        pss.setString(18, values.get("是否已返回"));
        pss.setString(19, values.get("补贴标准"));
        pss.setString(20, values.get("借出/借用通知"));
        pss.setString(21, values.get("借出/借用备注"));

        pss.setString(22, values.get("出生时间"));
        pss.setString(23, values.get("籍贯"));
        pss.setString(24, values.get("民族"));
        pss.setString(25, values.get("性别"));
        pss.setString(26, values.get("工作时间"));
        pss.setString(27, values.get("政治面貌"));
        pss.setString(28, values.get("加入时间"));
        pss.setString(29, values.get("技术职称"));
        pss.setString(30, values.get("取得时间"));
        pss.setString(31, values.get("职务"));
        pss.setString(32, values.get("职务级别"));
        pss.setString(33, values.get("任职时间"));
        pss.setString(34, values.get("岗位"));
        pss.setString(35, values.get("岗位级别"));
        pss.setString(36, values.get("身份"));
        pss.setString(37, values.get("现有学历"));
        pss.setString(38, values.get("毕业学校"));
        pss.setString(39, values.get("所学专业"));
        pss.setString(40, values.get("毕业时间"));
        pss.setString(41, values.get("工作学历"));
        pss.setString(42, values.get("毕业学校"));
        pss.setString(43, values.get("所学专业"));
        pss.setString(44, values.get("毕业时间"));
        pss.setString(45, values.get("职业资格等级"));
        pss.setString(46, values.get("专业技术资格名称"));
        pss.setString(47, values.get("专家人才类型"));
        pss.setString(48, values.get("预留1"));
        pss.setString(49, values.get("预留2"));
        pss.setString(50, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(51, time);
        pss.setString(52, time);
        pss.executeUpdate();
    }

    public void updateLend(Connection conn, Map<String,String> values) throws SQLException {
        String sql = "update lend set field1=?,field2=?,field3=?,field4=?,field5=?,field6=?,field7=?,field8=?,field9=?,field10=?," +
                "field11=?,field12=?,field13=?,field14=?,field15=?,field16=?,field17=?,field18=?,field19=?,field20=?,"
            + "field21=?,field22=?,field23=?,field24=?,field25=?,field26=?,field27=?,field28=?,field29=?,field30=?,"
            + "field31=?,field32=?,field33=?,field34=?,field35=?,field36=?,field37=?,field38=?,field39=?,field40=?,"
            + "field41=?,field42=?,field43=?,field44=?,field45=?,field46=?,field47=?,field48=?,field49=?,updatetime=?,deletetime='' where field2=?";
        PreparedStatement pss = conn.prepareStatement(sql);
        pss.setString(1, values.get("姓名"));
        pss.setString(2, values.get("身份证号"));
        pss.setString(3, values.get("单位"));
        pss.setString(4, values.get("部门"));
        pss.setString(5, values.get("部室/班组"));
        pss.setString(6, values.get("岗位"));
        pss.setString(7, values.get("性质"));
        pss.setString(8, values.get("借出/用单位"));
        pss.setString(9, values.get("借出/用部门"));
        pss.setString(10, values.get("借出/用班组"));
        pss.setString(11, values.get("借出/用岗位"));
        pss.setString(12, values.get("借出/用时间"));
        pss.setString(13, values.get("结束时间"));
        pss.setString(14, values.get("期限"));
        pss.setString(15, values.get("已借出/用时间"));
        pss.setString(16, values.get("剩余时间"));
        pss.setString(17, values.get("是否已返回"));
        pss.setString(18, values.get("补贴标准"));
        pss.setString(19, values.get("借出/借用通知"));
        pss.setString(20, values.get("借出/借用备注"));

        pss.setString(21, values.get("出生时间"));
        pss.setString(22, values.get("籍贯"));
        pss.setString(23, values.get("民族"));
        pss.setString(24, values.get("性别"));
        pss.setString(25, values.get("工作时间"));
        pss.setString(26, values.get("政治面貌"));
        pss.setString(27, values.get("加入时间"));
        pss.setString(28, values.get("技术职称"));
        pss.setString(29, values.get("取得时间"));
        pss.setString(30, values.get("职务"));
        pss.setString(31, values.get("职务级别"));
        pss.setString(32, values.get("任职时间"));
        pss.setString(33, values.get("岗位"));
        pss.setString(34, values.get("岗位级别"));
        pss.setString(35, values.get("身份"));
        pss.setString(36, values.get("现有学历"));
        pss.setString(37, values.get("毕业学校"));
        pss.setString(38, values.get("所学专业"));
        pss.setString(39, values.get("毕业时间"));
        pss.setString(40, values.get("工作学历"));
        pss.setString(41, values.get("毕业学校"));
        pss.setString(42, values.get("所学专业"));
        pss.setString(43, values.get("毕业时间"));
        pss.setString(44, values.get("职业资格等级"));
        pss.setString(45, values.get("专业技术资格名称"));
        pss.setString(46, values.get("专家人才类型"));
        pss.setString(47, values.get("预留1"));
        pss.setString(48, values.get("预留2"));
        pss.setString(49, values.get("预留3"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        pss.setString(50, time);
        pss.setString(51, values.get("身份证号"));
        pss.executeUpdate();
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