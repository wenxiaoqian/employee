package cn.hzmkj.employee;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息管理
 */
@WebServlet("/home/user")
public class UserServlet extends BaseServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String operation = req.getParameter("op");
        String method = req.getMethod();

        if("list".equals(operation)){
            list(req,resp);
        }else if("add".equals(operation) && "GET".equals(method)){
            addBefore(req,resp);
        }else if("add".equals(operation) && "POST".equals(method)){
            add(req,resp);
        }else if("update".equals(operation) && "GET".equals(method)){
            updateBefore(req,resp);
        }else if("update".equals(operation) && "POST".equals(method)){
            update(req,resp);
        }else if("delete".equals(operation)){
            deleteUser(req,resp);
        }else if("userlist".equals(operation)) {
            userlist(req, resp);
        }else {
            list(req, resp);
        }

    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userId = req.getParameter("userId");

        PrintWriter out = resp.getWriter();
        if (StringUtils.isBlank(userId)) {
            out.print("删除失败");
            return;
        }
        boolean result = userService.deleteUser(Integer.parseInt(userId));
        if (result) {
            out.print("success");
        } else {
            out.print("删除失败");
        }
    }



    /**
     * 添加
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/adduser.jsp").forward(req,resp);
    }

    /**
     * 添加
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("uname");
        String userpwd = req.getParameter("userpwd");
        String[] roles = req.getParameterValues("roleId");
        String roleId = StringUtils.join(roles,",");
        boolean result = userService.addUser(username,userpwd,roleId);
        PrintWriter out = resp.getWriter();
        if (result) {
            out.print("success");
        } else {
            out.print("添加失败");
        }
    }

    /**
     * 修改
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updateBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userId = req.getParameter("userId");

        UserBean userBean = userService.loadUser(userId);
        req.setAttribute("user",userBean);

        req.getRequestDispatcher("/user/edituser.jsp").forward(req,resp);

    }

    /**
     * 修改
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userId = req.getParameter("userId");
        String username = req.getParameter("uname");
        String iphone = req.getParameter("iphone");
        String userpwd = req.getParameter("userpwd");
        String[] roles = req.getParameterValues("roleId");
        String roleId = StringUtils.join(roles,",");

        boolean result = userService.updateUser(Integer.parseInt(userId),username,userpwd,roleId);
        PrintWriter out = resp.getWriter();
        if (result) {
            out.print("success");
        } else {
            out.print("修改失败");
        }
    }

    /**
     * 用户列表
     * @param req
     * @param resp
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/user/list.jsp").forward(req,resp);
    }

    private void userlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<HashMap> userList = userService.queryList();
        req.setAttribute("userList",userList);
        req.getRequestDispatcher("/user/userlist.jsp").forward(req,resp);
    }
}
