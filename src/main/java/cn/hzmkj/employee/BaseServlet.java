package cn.hzmkj.employee;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/6/8.
 */
public class BaseServlet extends HttpServlet {

    public UserBean getUserSession(HttpServletRequest request){
        UserBean userSession = (UserBean) request.getSession().getAttribute("user");
        return userSession;
    }

    public void response(HttpServletResponse response,Object message) throws IOException {
        PrintWriter out = response.getWriter();
        String msg = JSON.toJSONString(message);
        out.print(msg);
        return;
    }
}
