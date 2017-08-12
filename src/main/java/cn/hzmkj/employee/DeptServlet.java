package cn.hzmkj.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
 * 部门信息管理
 */
@WebServlet("/home/dept")
public class DeptServlet extends BaseServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String operation = req.getParameter("op");
        String method = req.getMethod();

        if("list".equals(operation)){
            list(req,resp);
        }else if("deptList".equals(operation)){
            deptList(req, resp);
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
        }else {
            list(req, resp);
        }

    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

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
     * 添加
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        if(StringUtils.isNotBlank(pid)) {
            HashMap value = userService.loadDept(Integer.parseInt(pid));
            req.setAttribute("pid",pid);
            req.setAttribute("deptName", value.get("name"));
        }
        req.getRequestDispatcher("/dept/adddept.jsp").forward(req,resp);
    }

    /**
     * 添加
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

    /**
     * 修改
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updateBefore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

        req.getRequestDispatcher("/dept/editdept.jsp").forward(req,resp);

    }

    /**
     * 修改
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
     * 部门列表
     * @param req
     * @param resp
     */
    private void deptList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<HashMap> values = userService.loadAllDept();
        StringBuilder json = new StringBuilder();
        json.append("[");
        for(HashMap value : values){
            String open = "";
            if("0".equals(value.get("pid"))){
                //open = ",open:true";
            }
            json.append("{ id:"+value.get("id")+", pId:"+value.get("pid")+", name:\"" + value.get("pno") + "-" +value.get("name")+"\""+open+"},");
        }
        if(json.indexOf(",") > -1){
            json.delete(json.length()-1,json.length());
        }
        json.append("]");
        req.setAttribute("json",json.toString());

        req.getRequestDispatcher("/dept/deptlist.jsp").forward(req,resp);
    }

    /**
     * 部门
     * @param req
     * @param resp
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/dept/list.jsp").forward(req,resp);
    }

}
