package web;

import controller.ControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import services.actionProviders.RequestActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Request/*")
public class RequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String token = req.getParameter("token");
        String param = req.getParameter("param");
        PrintWriter out = resp.getWriter();


        switch (pathInfo) {
            case "/getList":
            {
                out.println(getRequestListHTML(token));
                break;
            }
            case "/show":
            case "/add":
            case "/update":
            {

                out.println(getRequestHTML(param, token));
                break;
            }
            default:
                out.println(pathInfo + " default");
                break;
        }
    }

    private static String getRequestHTML(String param, String token) {
        Action showRequest = ActionProviderContainer
                .getRequestActionProvider()
                .getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showRequest, null, token);
        StringBuilder stringBuilder = new StringBuilder("<div style='padding-top: 10px;padding-bottom: 30px'></div><div class='row g-3'>");
        for (ViewField viewField: viewModel.getParameters()) {
            if (viewField.isDisplayable()) {
                String fieldId = viewField.isList()
                        ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
                        :  viewField.getAttributeName().replace(' ', '_');
                stringBuilder.append("<div class=col-md-4>")
                                .append("<label class='form-label' for='")
                                    .append(fieldId).append("'>")
                                    .append(viewField.getAttributeName())
                                .append("</label>");
                if (viewField.isList()) {
                    stringBuilder.append("<select class='form-control' id='").append(fieldId).append("'")
                                        .append(" data-ajax--cache='true' data-ajax--url='").append(viewField.getDataSource()).append("?token=").append(token).append("'>")
                                        .append("<option selected='selected' value='").append(viewField.getValueFrom().getAttributeValue()).append("'>")
                                        .append(viewField.getAttributeValue()).append("</option>")
                                .append("</select>");
                } else {
                    stringBuilder
                            .append("<input type='text' class='form-control'")
                            .append(" value='").append(viewField.getAttributeValue()).append("'")
                            .append(" id='").append(fieldId).append("'")
                            .append(!viewField.isChangeable() ? " disabled='true'" : "")
                            .append("/>");
                }
                stringBuilder.append("</div>");
            }
        }
        StringBuilder buttonBuilder = new StringBuilder("<div class='d-grid gap-2 d-md-block'>");
        stringBuilder.append("</div><div class='row g-3'>");
        for (Action action: viewModel.getActionsList())
        {
            if (action.isListItem()) {
                String[] permissionData = action.getActionName().split(" -> ");
                stringBuilder.append("<div class=col-md-3>")
                        .append("<label class='form-label' for='")
                        .append(permissionData[0]).append("'>")
                        .append(permissionData[0])
                        .append("</label>");
                stringBuilder
                        .append("<select class='form-control' id='").append(action.getActionName()).append("'")
                        .append(" data-ajax--cache='true' data-ajax--url='").append(action.getDataSource())
                        .append("?token=").append(token).append("&param=").append(action.getParameter()).append("'>")
                        .append("<option selected='selected' value='").append(permissionData[1]).append("'>").append(permissionData[1]).append("</option>")
                        .append("</select>")
                        .append("</div>");
            } else if (action.getActionName() != null ){
                buttonBuilder.append("<button type='button' class='btn btn-primary btn-sm' onclick='getContent(\"").append(
                                action.getRoute()).append("?token=").append(token).append("&param=").append(action.getParameter()).append("\");'>")
                        .append(action.getActionName()).append("</button>");
            }
        }
        stringBuilder.append("</div>");
        buttonBuilder.append("</div>");
        return stringBuilder.toString() + "<div> </div>" + buttonBuilder.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private String getRequestListHTML(String token) {
        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();

        Action getList = requestActionProvider.getActionList(null, null, null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(getList, null, token);
        StringBuilder buttonBuilder = new StringBuilder("<div style='padding-top: 10px;padding-bottom: 10px'>");
        StringBuilder listBuilder = new StringBuilder("<div class='list-group'>");

        for (Action action: viewModel.getActionsList())
        {
            if (action.isListItem()) {
                listBuilder
                        .append("<a class='list-group-item list-group-item-action' href='#' onclick='getContent(\"")
                        .append(action.getRoute()).append("?token=").append(token).append("&param=").append(action.getParameter())
                        .append("\");'>")
                        .append(action.getActionName())
                        .append("</a>");
            } else if (action.isInteractive() && action.getActionName() != null) {
                buttonBuilder.append("<button type='button' class='btn btn-primary btn-sm' onclick='getContent(\"").append(
                        action.getRoute()).append("?token=").append(token).append("&param=").append(action.getParameter()).append("\");'>")
                .append(action.getActionName()).append("</button>");
            }
        }
        listBuilder.append("</div>");
        buttonBuilder.append("</div>");


        return buttonBuilder.toString() + listBuilder.toString();
    }
}
