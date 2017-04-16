package cn.hzmkj.employee;

import com.airplan.bean.UserBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 添加用户
     * @return
     */
    public boolean addUser(String userName, String iphone, String userPwd, String roleId, String deptId){
        Connection conn = DBTool.getConnection() ;
        String sql = "insert into t_user(username,iphone,password,role,dept_id) values(?,?,?,?,?)";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userName);
            pss.setString(2,iphone);
            pss.setString(3,userPwd);
            pss.setString(4,roleId);
            pss.setString(5,deptId);
            int n = pss.executeUpdate();
            if(n > 0){
                return true;
            }
        }catch (Exception e){
            logger.error("UserService addUser :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return false;
    }

    /**
     * 修改用户
     * @return
     */
    public boolean updateUser(int userId, String userName, String iphone, String userPwd, String roleId){
        Connection conn = DBTool.getConnection() ;

        String sql = "update t_user set username=?,iphone=?,role=?";
        if(StringUtils.isNotBlank(userPwd)){
            sql += ",password=?";
        }
        sql += " where id=?";

        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userName);
            pss.setString(2,iphone);
            pss.setString(3,roleId);
            if(StringUtils.isNotBlank(userPwd)){
                pss.setString(4,userPwd);
                pss.setInt(5,userId);
            }else {
                pss.setInt(4, userId);
            }
            int n = pss.executeUpdate();
            if(n > 0){
                return true;
            }
        }catch (Exception e){
            logger.error("UserService updateUser :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return false;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    public boolean deleteUser(int userId) {
        Connection conn = DBTool.getConnection() ;
        String sql = "delete from t_user where id=? ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setInt(1,userId);
            int n = pss.executeUpdate();
            if(n > 0){
                sql = "delete from t_question_notes where question_id in (select id from t_question where create_by=?)";
                pss = conn.prepareStatement(sql);
                pss.setInt(1,userId);
                pss.executeUpdate();

                sql = "delete from t_question where create_by=?";
                pss = conn.prepareStatement(sql);
                pss.setInt(1,userId);
                pss.executeUpdate();
            }
            conn.commit();
        }catch (Exception e){
            logger.error("UserService deleteUser :" ,e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            DBTool.closeConnection(conn);
        }
        return true;
    }

    /**
     * 禁用用户
     * @param userId
     * @return
     */
    public boolean forbiddenUser(int userId,int status) {
        Connection conn = DBTool.getConnection() ;
        String sql = "update t_user set status = ? where id=? ";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setInt(1,status);
            pss.setInt(2,userId);
            pss.executeUpdate();
        }catch (Exception e){
            logger.error("UserService forbiddenUser :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return true;
    }

    /**
     * 查询用户
     * @return
     */
    public List<HashMap> queryList (int deptId, String keyword){
        Connection conn = DBTool.getConnection() ;
        String sql = "select user.username as name,user.id,user.iphone,user.role,dept.dept_name,user.status from t_user user join t_depart dept on user.dept_id=dept.id where 1=1 ";
        if(deptId != 0){
            sql += " and user.dept_id = "+deptId;
        }
        if(StringUtils.isNotBlank(keyword)){
            sql += " and (user.username like '%"+keyword+"%' or user.iphone like '%"+keyword+"%')";
        }
        sql += " order by user.id asc";
        List<HashMap> values = new ArrayList<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                HashMap<String,String> value = new HashMap<>();
                value.put("id",rss.getString("id"));
                value.put("name",rss.getString("name"));
                value.put("iphone",rss.getString("iphone"));
                value.put("deptName",rss.getString("dept_name"));
                value.put("roleId",rss.getString("role"));
                value.put("status",rss.getString("status"));
                values.add(value);
            }
        }catch (Exception e){
            logger.error("UserService queryList :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return values;
    }

    /**
     * 查询用户
     * @return
     */
    public HashMap<String,String> queryMap (){
        Connection conn = DBTool.getConnection() ;
        String sql = "select * from t_user user ";
        HashMap<String,String> value = new HashMap<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                value.put(rss.getString("id"),rss.getString("username"));
            }
        }catch (Exception e){
            logger.error("UserService queryMap :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return value;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public UserBean login(String username, String password){

        Connection conn = DBTool.getConnection() ;
        String sql = "select * from t_user where (username = ? or iphone = ?) and password=?";
        UserBean userBean = null;
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,username);
            pss.setString(2,username);
            pss.setString(3,password);
            ResultSet rss = pss.executeQuery();
            if(rss.next()){
                userBean = new UserBean();
                userBean.setId(rss.getString("id"));
                userBean.setPassword(rss.getString("password"));
                userBean.setUsername(rss.getString("username"));
                userBean.setIphone(rss.getString("iphone"));
                userBean.setDeptId(rss.getString("dept_id"));
                userBean.setRole(rss.getString("role"));
                userBean.setStatus(rss.getInt("status"));
            }
        }catch (Exception e){
            logger.error("UserService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return userBean;
    }

    /**
     * 加载用户信息
     * @param userId
     * @return
     */
    public UserBean loadUser(String userId){

        Connection conn = DBTool.getConnection() ;
        String sql = "select * from t_user where id = ?";
        UserBean userBean = null;
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userId);
            ResultSet rss = pss.executeQuery();
            if(rss.next()){
                userBean = new UserBean();
                userBean.setId(rss.getString("id"));
                userBean.setPassword(rss.getString("password"));
                userBean.setUsername(rss.getString("username"));
                userBean.setIphone(rss.getString("iphone"));
                userBean.setDeptId(rss.getString("dept_id"));
                userBean.setRole(rss.getString("role"));
            }
        }catch (Exception e){
            logger.error("UserService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return userBean;
    }

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    public boolean updatePassword(String userId, String password){
        Connection conn = DBTool.getConnection() ;
        String sql = "update t_user set password = ? where id=?";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,password);
            pss.setString(2,userId);
            int n = pss.executeUpdate();
            if(n > 0){
                return true;
            }
        }catch (Exception e){
            logger.error("LoginService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return false;
    }

    /**
     * 加载所有的部门
     * @return
     */
    public List<HashMap> loadAllDept (){
        Connection conn = DBTool.getConnection() ;
        String sql = "select * from t_depart order by dept_no asc";
        List<HashMap> values = new ArrayList<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                HashMap<String,String> value = new HashMap<>();
                value.put("id",rss.getString("id"));
                value.put("pid",rss.getString("parent_id"));
                value.put("name",rss.getString("dept_name"));
                value.put("no",rss.getString("dept_no"));
                values.add(value);
            }
        }catch (Exception e){
            logger.error("UserService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return values;
    }

    /**
     * 加载所有的部门
     * @return
     */
    public HashMap loadDept (int keyId){
        Connection conn = DBTool.getConnection() ;
        String sql = "select * from t_depart where id=?";
        HashMap<String,String> value = new HashMap<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setInt(1,keyId);
            ResultSet rss = pss.executeQuery();
            if(rss.next()){

                value.put("id",rss.getString("id"));
                value.put("pid",rss.getString("parent_id"));
                value.put("name",rss.getString("dept_name"));
                value.put("no",rss.getString("dept_no"));
            }
        }catch (Exception e){
            logger.error("UserService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return value;
    }

    /**
     * 修改部门
     * @return
     */
    public boolean updateDept (String deptName, String deptNo, int keyId){
        Connection conn = DBTool.getConnection() ;
        String sql = "update t_depart set dept_name=?,dept_no=? where id=?";
        HashMap<String,String> value = new HashMap<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,deptName);
            pss.setString(2,deptNo);
            pss.setInt(3,keyId);
            int num = pss.executeUpdate();
            if(num <= 0){
                return false;
            }
        }catch (Exception e){
            logger.error("UserService updateDept :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return true;
    }

    /**
     * 添加部门
     * @param deptName
     * @param deptNo
     * @param parentId
     * @return
     */
    public boolean addDept(String deptName, String deptNo, int parentId){
        Connection conn = DBTool.getConnection() ;
        String sql = "insert into t_depart(dept_name,dept_no,parent_id) values(?,?,?)";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,deptName);
            pss.setString(2,deptNo);
            pss.setInt(3,parentId);
            int n = pss.executeUpdate();
            if(n > 0){
                return true;
            }
        }catch (Exception e){
            logger.error("UserService addDept :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return false;
    }

    /**
     * 删除部门
     * @param pid
     * @return
     */
    public boolean deleteDept(int pid) {
        Connection conn = DBTool.getConnection() ;
        String sql = "delete from t_depart where id=? or parent_id = ?";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setInt(1,pid);
            pss.setInt(2,pid);
            int n = pss.executeUpdate();
            if(n > 0){
                return true;
            }
        }catch (Exception e){
            logger.error("UserService addDept :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return false;
    }
}
