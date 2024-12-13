package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import models.entities.ListItem;
import services.ListService;

import java.io.IOException;

@WebServlet("/list/*")
public class OptionsListServlet extends HttpServlet {

    @AllArgsConstructor
    class JSONResult {
        public ListItem[] results;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String token = req.getParameter("token");
        String list = "";
        switch (pathInfo) {
            case "/status":
                list = getStatusList(token);
                break;
            case "/type":
                list = getTypeList(token);
                break;
            case "/resident":
                list = getResidentList(token);
                break;
            case "/permission":
                list = getPermissionList(token);
        }
        resp.setContentType("application/json");
        resp.getWriter().print(list);
    }

    private String getResidentList(String token) {
        ListItem[] list  = ListService.getResidentsList().toArray(new ListItem[0]);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(new JSONResult(list));
        } catch (Exception e) {
            return "";
        }
    }

    private String getStatusList(String token) {
        ListItem[] list  = ListService.getStatusesList().toArray(new ListItem[0]);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(new JSONResult(list));
        } catch (Exception e) {
            return "";
        }
    }

    private String getTypeList(String token) {
        ListItem[] list = ListService.getTypesList().toArray(new ListItem[0]);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(new JSONResult(list));
        }
        catch (Exception e) {
            return "";
        }
    }

    private String getPermissionList(String token) {
        ListItem[] list = ListService.getPermissionLevelsList().toArray(new ListItem[0]);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(new JSONResult(list));
        }
        catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
