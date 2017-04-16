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
        }else if("topwd".equals(operation)){
            topassword(req,resp);
        }else if("password".equals(operation)){
            updatePassword(req,resp);
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
        }else if("forbidden".equals(operation)){
            forbiddenUser(req,resp);
        }else if("addDept".equals(operation) && "GET".equals(method)){
            addDeptBefore(req,resp);
        }else if("addDept".equals(operation) && "POST".equals(method)){
            addDept(req,resp);
        }else if("updateDept".equals(operation) && "GET".equals(method)){
            updateDeptBefore(req,resp);
        }else if("updateDept".equals(operation) && "POST".equals(method)){
            updateDept(req,resp);
        }else if("delDept".equals(operation)){
            delDept(req, resp);
        }else if("userlist".equals(operation)) {
            userlist(req, resp);
        }else if("deptList".equals(operation)){
            deptlist(req, resp);
        }else {
            list(req, resp);
        }

    }

    private void topassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/mobile/password.jsp").forward(req,resp);
    }

    private void deptlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<HashMap> values = userService.loadAllDept();
        StringBuilder json = new StringBuilder();
        json.append("[");
        for(HashMap value : values){
            String open = "";
            if("0".equals(value.get("pid"))){
                open = ",open:true";
            }
            json.append("{ id:"+value.get("id")+", pId:"+value.get("pid")+", name:\""+value.get("no") + "-" +value.get("name")+"\""+open+"},");
        }
        if(json.indexOf(",") > -1){
            json.delete(json.length()-1,json.length());
        }
        json.append("]");
        req.setAttribute("json",json.toString());
        req.getRequestDispatcher("/user/deptlist.jsp").forward(req,resp);
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
     * 禁用用户
     * @param req
     * @param resp
     * @throws IOException
     */
    private void forbiddenUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String userId = req.getParameter("userId");
        PrintWriter out = resp.getWriter();
        if (StringUtils.isBlank(userId)) {
            out.print("禁用失败");
            return;
        }
        String strStatus = req.getParameter("status");
        int status = Integer.parseInt(strStatus);

        boolean result = userService.forbiddenUser(Integer.parseInt(userId),status);
        if (result) {
            out.print("success");
        } else {
            out.print("禁用失败");
        }
    }


    private void delDept(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String keyId = req.getParameter("keyId");

        PrintWriter out = resp.getWriter();
        int pid = Integer.parseInt(keyId);
        if (StringUtils.isBlank(keyId)) {
            out.print("删除失败");
            return;
        }
        boolean result = userService.deleteDept(pid);
        if (result) {
            out.print("success");
        } else {
            out.print("删除失败");
        }
    }

    /**
     * 添加部门之前
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addDeptBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pid = req.getParameter("pid");
        if(StringUtils.isNotBlank(pid)) {
            HashMap value = userService.loadDept(Integer.parseInt(pid));
            req.setAttribute("pid",pid);
            req.setAttribute("deptName", value.get("name"));
        }
        req.getRequestDispatcher("/user/adddept.jsp").forward(req,resp);
    }

    private void addDept(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parentId = req.getParameter("parentId");
        String deptName = req.getParameter("deptName");
        String deptNo = req.getParameter("deptNo");

        boolean result = userService.addDept(deptName, deptNo, Integer.parseInt(parentId));
        PrintWriter out = resp.getWriter();
        if (result) {
            out.print("success");
        } else {
            out.print("添加失败");
        }
    }

    private void updateDeptBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String keyId = req.getParameter("keyId");
        if(StringUtils.isNotBlank(keyId)) {
            HashMap<String,String> value = userService.loadDept(Integer.parseInt(keyId));
            if(value != null){
                HashMap parentDept = userService.loadDept(Integer.parseInt(value.get("pid")));
                req.setAttribute("pid",value.get("pid"));
                req.setAttribute("deptName",parentDept.get("name"));
            }
            req.setAttribute("value", value);
        }
        req.getRequestDispatcher("/user/editdept.jsp").forward(req,resp);
    }

    private void updateDept(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String deptName = req.getParameter("deptName");
        String deptNo = req.getParameter("deptNo");

        boolean result = userService.updateDept(deptName, deptNo, Integer.parseInt(id));
        PrintWriter out = resp.getWriter();
        if (result) {
            out.print("success");
        } else {
            out.print("修改失败");
        }
    }

    /**
     * 修改密码
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String password = req.getParameter("password");
        String oldpassword = req.getParameter("oldpassword");

        PrintWriter out = resp.getWriter();
        HashMap<String,Object> result = new HashMap<String,Object>();
        //step1 验证数据安全性
        if(StringUtils.isBlank(password) || StringUtils.isBlank(oldpassword)){
            String msg = "原密码和新密码不能为空";
            out.print(msg);
        }

        //step2 判断原密码是否正确
        UserBean userSession = getUserSession(req);
        if(userSession.getPassword().equals(oldpassword)){
            String msg = "原密码错误！";
            out.print(msg);
        }

        //step3 修改密码
        if(userService.updatePassword(userSession.getId(),password)){
            out.print("success");
        }else{
            String msg = "修改密码失败！";
            out.print(msg);
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

        String deptId = req.getParameter("deptId");
        if(StringUtils.isNotBlank(deptId)) {
            HashMap value = userService.loadDept(Integer.parseInt(deptId));
            req.setAttribute("deptName", value.get("name"));
        }
        req.setAttribute("deptId",deptId);

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
        String iphone = req.getParameter("iphone");
        String userpwd = req.getParameter("userpwd");
        String deptId = req.getParameter("deptId");
        String[] roles = req.getParameterValues("roleId");
        String roleId = StringUtils.join(roles,",");
        boolean result = userService.addUser(username,iphone,userpwd,roleId,deptId);
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

        HashMap value = userService.loadDept(Integer.parseInt(userBean.getDeptId()));
        req.setAttribute("deptName", value.get("name"));

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

        boolean result = userService.updateUser(Integer.parseInt(userId),username,iphone,userpwd,roleId);
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


        List<HashMap> values = userService.loadAllDept();
        StringBuilder json = new StringBuilder();
        json.append("[");
        for(HashMap value : values){
            String open = "";
            if("0".equals(value.get("pid"))){
                open = ",open:true";
            }
            json.append("{ id:"+value.get("id")+", pId:"+value.get("pid")+", name:\""+value.get("no") + "-" +value.get("name")+"\""+open+"},");
        }
        if(json.indexOf(",") > -1){
            json.delete(json.length()-1,json.length());
        }
        json.append("]");
        req.setAttribute("json",json.toString());
        req.getRequestDispatcher("/user/list.jsp").forward(req,resp);
    }

    private void userlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deptId = req.getParameter("deptId");
        req.setAttribute("deptId",deptId);

        int dId = 0;
        if(StringUtils.isNotBlank(deptId)){
            dId = Integer.parseInt(deptId);
        }

        String keyword = req.getParameter("keyword");
        List<HashMap> userList = userService.queryList(dId, keyword);
        req.setAttribute("userList",userList);
        req.getRequestDispatcher("/user/userlist.jsp").forward(req,resp);
    }
}
