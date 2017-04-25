package cn.hzmkj.employee;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * 登录验证
 *
 */
@WebServlet(value = "/login")
public class LoginServlet  extends BaseServlet{

    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = req.getParameter("username");
        String userPwd = req.getParameter("password");

        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        if(StringUtils.isBlank(userName) || StringUtils.isBlank(userPwd)){
            out.print("failure");
            return;
        }
        UserBean user = userService.login(userName,userPwd);
        if(user != null){
            req.getSession().setAttribute("user", user);
            out.print("success");

        }else{
            out.print("用户名或密码错误!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/logn.jsp").forward(req,resp);
    }
}
