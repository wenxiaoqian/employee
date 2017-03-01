package cn.hzmkj.employee;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxq-mac on 16/12/17.
 */
@WebServlet("/api/login")
public class UserServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sername = req.getParameter("uname");
        String password = req.getParameter("password");

        PrintWriter out = resp.getWriter();
        String result = "";
        Map<String,String> userInfo = existUser(sername, password);
        if(userInfo.get("id") != null){
            result = JSON.toJSONString(userInfo);
        }
        out.print(result);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public Map<String,String> existUser(String username, String password){
        Map<String,String> map = new HashMap<String, String>();
        try {
            String sql = "select * from account where account=? and password=?";
            PreparedStatement pss = DBTool.getConnection().prepareStatement(sql);
            pss.setString(1, username);
            pss.setString(2, password);
            ResultSet rss = pss.executeQuery();
            if (rss.next()) {
                map.put("id", rss.getString("id"));
                map.put("account", rss.getString("account"));
                map.put("password", rss.getString("password"));
            }
            pss.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return map;
    }
}
