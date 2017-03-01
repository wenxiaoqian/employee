package cn.hzmkj.employee;

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

/**
 * 远程数据修改API
 */
@WebServlet(value = "/api/emp")
public class EmpServlet extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(EmpServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sql = req.getParameter("sql");

        PrintWriter out = resp.getWriter();
        String result = "success";
        Connection conn = null;
        try {
            conn = DBTool.getConnection();
            DBTool.executeSQL(conn, sql);
        }catch (Exception ex){
            ex.printStackTrace();
            result = "failure";
        }finally {
            DBTool.closeConnection(conn);
        }
        logger.info("emp sql:"+sql);
        out.print(result);
        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
