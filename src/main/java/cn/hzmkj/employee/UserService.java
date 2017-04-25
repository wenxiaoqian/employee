package cn.hzmkj.employee;

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
    public boolean addUser(String userName, String userPwd, String roleId){
        Connection conn = DBTool.getConnection() ;
        String sql = "insert into account(account,password,roleId) values(?,?,?)";
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userName);
            pss.setString(3,userPwd);
            pss.setString(4,roleId);
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
    public boolean updateUser(int userId, String userName, String userPwd, String roleId){
        Connection conn = DBTool.getConnection() ;

        String sql = "update account set account=?,roleId=?";
        if(StringUtils.isNotBlank(userPwd)){
            sql += ",password=?";
        }
        sql += " where id=?";

        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userName);
            pss.setString(2,roleId);
            if(StringUtils.isNotBlank(userPwd)){
                pss.setString(3,userPwd);
                pss.setInt(4,userId);
            }else {
                pss.setInt(3, userId);
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
        String sql = "delete from account where id=? ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setInt(1,userId);
            pss.executeUpdate();
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
     * 查询用户
     * @return
     */
    public List<HashMap> queryList (){
        Connection conn = DBTool.getConnection() ;
        String sql = "select * from account";
        List<HashMap> values = new ArrayList<>();
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            ResultSet rss = pss.executeQuery();
            while(rss.next()){
                HashMap<String,String> value = new HashMap<>();
                value.put("id",rss.getString("id"));
                value.put("account",rss.getString("account"));
                value.put("roleId",rss.getString("roleId"));
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
        String sql = "select * from account where account = ? and password=?";
        UserBean userBean = null;
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,username);
            pss.setString(2,password);
            ResultSet rss = pss.executeQuery();
            if(rss.next()){
                userBean = new UserBean();
                userBean.setId(rss.getString("id"));
                userBean.setPassword(rss.getString("password"));
                userBean.setAccount(rss.getString("account"));
                userBean.setRoleId(rss.getString("roleId"));
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
        String sql = "select * from account where id = ?";
        UserBean userBean = null;
        try {
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1,userId);
            ResultSet rss = pss.executeQuery();
            if(rss.next()){
                userBean = new UserBean();
                userBean.setId(rss.getString("id"));
                userBean.setPassword(rss.getString("password"));
                userBean.setAccount(rss.getString("account"));
                userBean.setRoleId(rss.getString("roleId"));
            }
        }catch (Exception e){
            logger.error("UserService login :" ,e);
        }finally {
            DBTool.closeConnection(conn);
        }
        return userBean;
    }

}
