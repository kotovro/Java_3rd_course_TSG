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
import java.util.Arrays;

@WebServlet("/Role/*")
public class RoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String token = WebUtils.sanitizeOutput(req.getParameter("token"));
        String param = WebUtils.sanitizeOutput(req.getParameter("param"));
        PrintWriter out = resp.getWriter();


        switch (pathInfo) {
            case "/getList": {
                out.println(getRoleListHTML(token));
                break;
            }
            case "/show":
            case "/add": {

                out.println(getRoleHTML(param, token));
                break;
            }
            case "/update": {
                String error = getRoleUpdateResult(req);
                if(!error.isEmpty()) {
                    out.println(error);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                break;
            }
            case "/delete": {
                deleteRole(param, token);
                out.println(getRoleListHTML(token));
                break;
            }

            default:
                out.println(pathInfo + " default");
                break;
        }
    }

    private void deleteRole(String param, String token) {
        Action deleteRole = ActionProviderContainer
                .getRoleActionProvider().getActionDelete(param, "", null, null);
        ControllerService controllerService = new ControllerService();
        controllerService.doAction(deleteRole, null, token);

    }

    private static String getRoleHTML(String param, String token) {
        Action showRole = ActionProviderContainer
                .getRoleActionProvider().getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showRole, null, token);

        String topBuffer = "<div style='padding-top: 10px;padding-bottom: 30px'></div>";
        String viewFieldsHTML = getPartHTMLViewFields(token, viewModel);
        viewFieldsHTML += getPartHTMLSelect(token, viewModel);
        String buttonsHTML = getPartHTMLButtons(token, viewModel);

        return topBuffer + viewFieldsHTML + buttonsHTML;
    }

    private static String getPartHTMLSelect(String token, ViewModel viewModel) {
        StringBuilder stringBuilder = new StringBuilder("<div class='row g-3'>");
        for (Action action : viewModel.getActionsList()) {
            if (action.isListItem()) {
                String[] permissionData = Arrays.stream(
                                action.getActionName().split(" -> "))
                        .map(WebUtils::sanitizeOutput)
                        .toArray(String[]::new);
                stringBuilder.append("<div class=col-md-3>")
                        .append("<label class='form-label' for='")
                        .append(permissionData[0]).append("'>")
                        .append(permissionData[0])
                        .append("</label>");
                stringBuilder
                        .append("<select class='form-control' id='").append(permissionData[0]).append("'")
                        .append(" data-ajax--cache='true' data-ajax--url='").append(WebUtils.sanitizeOutput(action.getDataSource()))
                        .append("?token=").append(token).append("&param=").append(WebUtils.sanitizeOutput(action.getParameter())).append("'>")
                        .append("<option selected='selected' value='").append(permissionData[1]).append("'>").append(permissionData[1]).append("</option>")
                        .append("</select>")
                        .append("</div>");
            }
        }
        stringBuilder.append("</div>");
        return stringBuilder.toString();
    }

    private static String getPartHTMLButtons(String token, ViewModel viewModel) {
        StringBuilder buttonBuilder = new StringBuilder("<div class='d-grid gap-2 d-md-block'>");
        for (Action action : viewModel.getActionsList()) {
            if (!action.isListItem() && action.getActionName() != null) {
                String onClickAction = action.getActionType() == Action.ActionType.UPDATE ? "updateContent(\"" : "getContent(\"";
                buttonBuilder/*.append("<div class=col-md-1>")*/
                        .append("<button type='button' class='btn btn-primary btn-sm' onclick='").append(onClickAction).append(
                                WebUtils.sanitizeOutput(action.getRoute())).append("?token=").append(token).append("&param=").append(WebUtils.sanitizeOutput(action.getParameter())).append("\");'>")
                        .append(WebUtils.sanitizeOutput(action.getActionName()))
                        .append("</button>");
                //.append("</div>");
            }
        }
        buttonBuilder.append("</div>");
        return buttonBuilder.toString();
    }

    private static String getPartHTMLViewFields(String token, ViewModel viewModel) {
        StringBuilder stringBuilder = new StringBuilder("<div class='row g-3'>");
        for (ViewField viewField : viewModel.getParameters()) {
            if (viewField.isDisplayable()) {
                String fieldId = WebUtils.sanitizeOutput(
                        viewField.isList()
                                ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
                                : viewField.getAttributeName().replace(' ', '_'));
                stringBuilder.append("<div class=col-md-4>")
                        .append("<label class='form-label' for='")
                        .append(fieldId).append("'>")
                        .append(WebUtils.sanitizeOutput(viewField.getAttributeName()))
                        .append("</label>");
                if (viewField.isList()) {
                    stringBuilder.append("<select class='form-control' id='").append(fieldId).append("'")
                            .append(" data-ajax--cache='true' data-ajax--url='").append(WebUtils.sanitizeOutput(viewField.getDataSource())).append("?token=").append(token).append("'>")
                            .append("<option selected='selected' value='").append(WebUtils.sanitizeOutput(viewField.getValueFrom().getAttributeValue())).append("'>")
                            .append(WebUtils.sanitizeOutput(viewField.getAttributeValue())).append("</option>")
                            .append("</select>");
                } else {
                    stringBuilder
                            .append("<input type='text' class='form-control'")
                            .append(" value='").append(WebUtils.sanitizeOutput(viewField.getAttributeValue())).append("'")
                            .append(" id='").append(fieldId).append("'")
                            .append(!viewField.isChangeable() ? " disabled='true'" : "")
                            .append("/>");
                }
                stringBuilder.append("</div>");
            }
            stringBuilder.append("</div>");
        }
        return stringBuilder.toString();
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String pathInfo = req.getPathInfo();
//        PrintWriter out = resp.getWriter();
//
//        switch (pathInfo) {
//            case "/update":
//            {
//                String error = getRoleUpdateResult(req);
//                if(!error.isEmpty()) {
//                    out.println(error);
//                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                }
//                break;
//            }
//
//            default:
//                out.println(pathInfo + " default");
//                break;
//        }
//    }

    private String getRoleUpdateResult(HttpServletRequest req) {
        String param = req.getParameter("param");
        String token = req.getParameter("token");
        Action showAction = ActionProviderContainer.getRoleActionProvider().getActionShow(param, null, null, null, false);
        ControllerService controllerService = new ControllerService();
        ViewModel vm = controllerService.doAction(showAction, null, token);
        boolean isRoleChanged = false;
        String errorMessage = "";

        for (ViewField viewField : vm.getParameters()) {
            if (viewField.isChangeable()) {
                String fieldId = WebUtils.sanitizeOutput(
                        viewField.isList()
                                ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
                                : viewField.getAttributeName().replace(' ', '_'));
                String value = req.getParameter(fieldId);

                if (value != null && !value.equals(viewField.getAttributeValue())) {
                    viewField.setAttributeValue(value);
                    isRoleChanged = true;
                }
            }
        }

        Action update = vm.getActionsList().stream()
                .filter(a -> a.getActionType() == Action.ActionType.UPDATE)
                .findFirst()
                .orElse(null);

        if (isRoleChanged && update != null) {
            vm = controllerService.doAction(update, vm, token);
            if (vm.getErrorMessage() != null) {
                return WebUtils.sanitizeOutput(vm.getErrorMessage());
            }
        }

        for (Action act : vm.getActionsList()) {
            if (act.isListItem()) {
                String[] permissionData = act.getActionName().split(" -> ");
                String value = req.getParameter(permissionData[0]);

                if (value != null && !value.equals(permissionData[1])) {
                    ViewModel permView = controllerService.doAction(act, null, token);
                    ViewField field = permView.getParameters()
                            .stream()
                            .filter(ViewField::isChangeable)
                            .findFirst()
                            .orElse(null);
                    if (field != null) {
                        field.setAttributeValue(value);
                        permView = controllerService.doAction(act.getOnSuccess(), permView, token);
                        if (permView.getErrorMessage() != null) {
                            errorMessage = permView.getErrorMessage();
                            break;
                        }
                    }
                }
            }
        }

        return WebUtils.sanitizeOutput(errorMessage);
    }

    private String getRoleListHTML(String token) {
        IActionProvider roleActionProvider = ActionProviderContainer.getRoleActionProvider();

        Action getList = roleActionProvider.getActionList(null, null, null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(getList, null, token);
        StringBuilder buttonBuilder = new StringBuilder("<div style='padding-top: 10px;padding-bottom: 10px'>");
        StringBuilder listBuilder = new StringBuilder("<div class='list-group'>");

        for (Action action : viewModel.getActionsList()) {
            if (action.isListItem()) {
                listBuilder
                        .append("<a class='list-group-item list-group-item-action' href='#' onclick='getContent(\"")
                        .append(WebUtils.sanitizeOutput(action.getRoute()))
                        .append("?token=").append(token)
                        .append("&param=").append(WebUtils.sanitizeOutput(action.getParameter()))
                        .append("\");'>")
                        .append(WebUtils.sanitizeOutput(action.getActionName()))
                        .append("</a>");
            } else if (action.isInteractive() && action.getActionName() != null) {
                buttonBuilder.append("<button type='button' class='btn btn-primary btn-sm' onclick='getContent(\"")
                        .append(WebUtils.sanitizeOutput(action.getRoute()))
                        .append("?token=").append(token)
                        .append("&param=").append(WebUtils.sanitizeOutput(action.getParameter()))
                        .append("\");'>")
                        .append(WebUtils.sanitizeOutput(action.getActionName()))
                        .append("</button>");
            }
        }
        listBuilder.append("</div>");
        buttonBuilder.append("</div>");


        return buttonBuilder.toString() + listBuilder.toString();
    }
}
