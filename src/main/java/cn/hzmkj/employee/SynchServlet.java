package cn.hzmkj.employee;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程数据同步API
 */
@WebServlet(value = "/api/synch")
public class SynchServlet extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(SynchServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lasttime = req.getParameter("lasttime");
        String userId = req.getParameter("userId");
        String type = req.getParameter("type");

        List<Map<String, String>> dataList = null;
        if("emp".equals(type)) {
            dataList = loadEmpData(lasttime, "");
        }else if("works".equals(type)){
            dataList = loadWorkData(lasttime, userId);
        }else if("family".equals(type)){
            dataList = loadFamilyData(lasttime,userId);
        }else if("edu".equals(type)){
            dataList = loadEduData(lasttime, userId);
        }else if("lend".equals(type)){
            dataList = loadLendData(lasttime, userId);
        }else if("dept".equals(type)){
            dataList = loadDeptData();
        }else if("opertion".equals(type)){
            dataList = loadOperation();
        }
        String json = JSON.toJSON(dataList).toString();

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

    public List<Map<String, String>> loadOperation(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from opertion ";
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
            logger.info("dept data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步人事信息
     * @param lasttime
     * @param deptIds
     * @return
     */
    public List<Map<String, String>> loadEmpData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from employee where updatetime > ?";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, lasttime);
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
            logger.info("emp data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步人事信息
     * @return
     */
    public List<Map<String, String>> loadDeptData(){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from department";
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
            logger.info("dept data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步工作经历信息
     * @param lasttime
     * @param deptIds
     * @return
     */
    public List<Map<String, String>> loadWorkData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from works where updatetime > ?";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, lasttime);
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
            logger.info("work data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步家庭信息
     * @param lasttime
     * @param deptIds
     * @return
     */
    public List<Map<String, String>> loadFamilyData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from family where updatetime > ?";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, lasttime);
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
            logger.info("family data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步教育信息
     * @param lasttime
     * @param deptIds
     * @return
     */
    public List<Map<String, String>> loadEduData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from educate where updatetime > ?";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, lasttime);
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
            logger.info("edu data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }

    /**
     * 同步借入借出信息
     * @param lasttime
     * @param deptIds
     * @return
     */
    public List<Map<String, String>> loadLendData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try{
            String sql = "select * from lend where updatetime > ?";
            Connection conn = DBTool.getConnection();
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, lasttime);
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
            logger.info("lend data:"+sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return dataList;
    }
}
