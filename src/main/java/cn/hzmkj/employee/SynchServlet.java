package cn.hzmkj.employee;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 远程数据同步API
 */
@WebServlet(value = "/api/synch")
public class SynchServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lasttime = req.getParameter("lasttime");
        String userId = req.getParameter("userId");

        List<Map<String, String>> dataList = loadData(lasttime, "");
        String json = JSON.toJSON(dataList).toString();

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

    public List<Map<String, String>> loadData(String lasttime, String deptIds){
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();


        return dataList;
    }
}
