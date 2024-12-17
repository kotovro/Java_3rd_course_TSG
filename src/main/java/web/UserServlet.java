package web;

import controller.ControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/User/*")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String token = req.getParameter("token");
        String param = req.getParameter("param");
        PrintWriter out = resp.getWriter();


        switch (pathInfo) {
            case "/getList":
            {
                resp.setContentType("application/json");
                out.println(getUserListHTML(token));
                break;
            }
            case "/show":
            {
                out.println(getUserHTML(param, token));
                break;
            }
            case "/add":
            {
                out.println(getAddUserHTML(param, token));
            }

            default:
                out.println(pathInfo + " default");
                break;
        }
    }

    private String getAddUserHTML(String param, String token) {
        Action addUser = ActionProviderContainer
                .getUserActionProvider().getActionAdd(param, "", null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(addUser, null, token);
        for (ViewField viewField: viewModel.getParameters()) {
            //add fileds from viewModel
        }
        return new String();
    }

    private static String getUserHTML(String param, String token) {
        Action showUser = ActionProviderContainer
                .getUserActionProvider().getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showUser, null, token);
        StringBuilder stringBuilder = new StringBuilder("<div style='padding-top: 10px;padding-bottom: 30px'></div><div class='row g-3'>");
        for (ViewField viewField: viewModel.getParameters()) {
            if (viewField.isDisplayable()) {
//                String fieldId = viewField.isList()
//                        ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
//                        :  viewField.getAttributeName().replace(' ', '_');
                stringBuilder.append("<div class=col-md-4>")
                        .append("<label class='form-label' for='")
                        .append(viewField.getAttributeName()).append("'>")
                        .append(viewField.getAttributeName())
                        .append("</label>");
                if (viewField.isList()) {
                    stringBuilder.append("<select class='form-control' id='").append(viewField.getAttributeValue()).append("'")
                            .append(" data-ajax--cache='true' data-ajax--url='").append(viewField.getDataSource()).append("?token=").append(token).append("'>")
                            .append("<option selected='selected' value='").append(viewField.getValueFrom().getAttributeValue()).append("'>")
                            .append(viewField.getAttributeValue()).append("</option>")
                            .append("</select>");
                } else {
                    stringBuilder
                            .append("<input type='text' class='form-control'")
                            .append(" value='").append(viewField.getAttributeValue()).append("'")
                            .append(" id='").append(viewField.getAttributeName()).append("'")
                            .append(!viewField.isChangeable() ? " disabled='true'" : "")
                            .append("/>");
                }
                stringBuilder.append("</div>");
            }
        }
        stringBuilder.append("</div>");
        return stringBuilder.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private String getUserListHTML(String token) {
        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();

        Action getList = userActionProvider.getActionList(null, null, null, null);
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
