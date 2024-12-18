package web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import web.page.elements.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
                out.println(WebUtils.generateList(
                        ActionProviderContainer.getUserActionProvider(), token));
                break;
            }
            case "/show":
            case "/add":
            {
                resp.setContentType("application/json");
                out.println(getUserHTML(param, token));
                break;
            }
            case "/update":
            {
                out.println(getUserUpdateResult(req));
                break;
            }
            default:
                out.println(pathInfo + " default");
                break;
        }
    }


//    private static String getUserHTML(String param, String token) {
//        Action showUser = ActionProviderContainer
//                .getUserActionProvider().getActionShow(param, "", null, null, true);
//        ControllerService controllerService = new ControllerService();
//        ViewModel viewModel = controllerService.doAction(showUser, null, token);
//        StringBuilder stringBuilder = new StringBuilder("<div style='padding-top: 10px;padding-bottom: 30px'></div><div class='row g-3'>");
//        for (ViewField viewField: viewModel.getParameters()) {
//            if (viewField.isDisplayable()) {
////                String fieldId = viewField.isList()
////                        ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
////                        :  viewField.getAttributeName().replace(' ', '_');
//                stringBuilder.append("<div class=col-md-4>")
//                        .append("<label class='form-label' for='")
//                        .append(viewField.getAttributeName()).append("'>")
//                        .append(viewField.getAttributeName())
//                        .append("</label>");
//                if (viewField.isList()) {
//                    stringBuilder.append("<select class='form-control' id='").append(viewField.getAttributeValue()).append("'")
//                            .append(" data-ajax--cache='true' data-ajax--url='").append(viewField.getDataSource()).append("?token=").append(token).append("'>")
//                            .append("<option selected='selected' value='").append(viewField.getValueFrom().getAttributeValue()).append("'>")
//                            .append(viewField.getAttributeValue()).append("</option>")
//                            .append("</select>");
//                } else {
//                    stringBuilder
//                            .append("<input type='text' class='form-control'")
//                            .append(" value='").append(viewField.getAttributeValue()).append("'")
//                            .append(" id='").append(viewField.getAttributeName()).append("'")
//                            .append(!viewField.isChangeable() ? " disabled='true'" : "")
//                            .append("/>");
//                }
//                stringBuilder.append("</div>");
//            }
//        }
//        stringBuilder.append("</div>");
//        return stringBuilder.toString();
//    }

    private static String getUserHTML(String param, String token) {
        Action showRole = ActionProviderContainer
                .getUserActionProvider().getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showRole, null, token);

        PageContent pageContent = new PageContent();
        pageContent.getInputs().addAll(getPartHTMLViewFields(token, viewModel));
        pageContent.getSelects().addAll(getPartHTMLSelect(token, viewModel));
        pageContent.getButtons().addAll(getPartHTMLButtons(token, viewModel));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(pageContent);
        } catch (Exception e) {
            return "";
        }
    }

    private static List<WebSelect> getPartHTMLSelect(String token, ViewModel viewModel) {
        List<WebSelect> selectList = new LinkedList<>();
        for (ViewField viewField : viewModel.getParameters()) {
            if (viewField.isDisplayable() && viewField.isList()) {

                String fieldId = WebUtils.sanitizeOutput(
                        viewField.getValueFrom().getAttributeName().replace(' ', '_'));
                selectList.add(new WebSelect(fieldId,
                        WebUtils.sanitizeOutput(viewField.getAttributeName()),
                        WebUtils.sanitizeOutput(viewField.getValueFrom().getAttributeValue()),
                        WebUtils.sanitizeOutput(viewField.getAttributeValue()),
                        WebUtils.sanitizeOutput(viewField.getDataSource()) + "?token=" + token));
            }
        }
        for (Action action : viewModel.getActionsList()) {
            if (action.isListItem()) {
                String[] permissionData = Arrays.stream(
                                action.getActionName().split(" -> "))
                        .map(WebUtils::sanitizeOutput)
                        .toArray(String[]::new);
                selectList.add(new WebSelect(
                        permissionData[0],
                        permissionData[0],
                        permissionData[1],
                        permissionData[1],
                        WebUtils.sanitizeOutput(action.getDataSource()) +
                                "?token=" + token + "&param=" + WebUtils.sanitizeOutput(action.getParameter())
                ));
            }
        }
        return selectList;
    }

    private static List<WebButton> getPartHTMLButtons(String token, ViewModel viewModel) {
        List<WebButton> webButtons = new LinkedList<>();
        for (Action action : viewModel.getActionsList()) {
            if (!action.isListItem() && action.getActionName() != null) {
                webButtons.add(new WebButton("",
                        WebUtils.sanitizeOutput(action.getActionName()),
                        WebUtils.sanitizeOutput(action.getRoute()) +
                                "?token=" + token +
                                "&param=" + WebUtils.sanitizeOutput(action.getParameter()),
                        action.getActionType() == Action.ActionType.UPDATE ? ButtonType.UPDATE : ButtonType.GET));
            }
        }
        return webButtons;
    }

    private static List<WebInput> getPartHTMLViewFields(String token, ViewModel viewModel) {
        List<WebInput> webInputs = new LinkedList<>();
        for (ViewField viewField : viewModel.getParameters()) {
            if (viewField.isDisplayable() && !viewField.isList()) {
                String fieldId = WebUtils.sanitizeOutput(
                        viewField.getAttributeName().replace(' ', '_'));
                webInputs.add(new WebInput(fieldId,
                        WebUtils.sanitizeOutput(viewField.getAttributeName()),
                        WebUtils.sanitizeOutput(viewField.getAttributeValue()),
                        viewField.isChangeable()));
            }
        }
        return webInputs;
    }

    private String getUserUpdateResult(HttpServletRequest req) {
        String param = req.getParameter("param");
        String token = req.getParameter("token");
        Action showAction = ActionProviderContainer.getUserActionProvider().getActionShow(param, null, null, null, false);
        ControllerService controllerService = new ControllerService();
        ViewModel vm = controllerService.doAction(showAction, null, token);
        boolean isUserChanged = false;
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
                    isUserChanged = true;
                }
            }
        }

        Action update = vm.getActionsList().stream()
                .filter(a -> a.getActionType() == Action.ActionType.UPDATE)
                .findFirst()
                .orElse(null);

        if (isUserChanged && update != null) {
            vm = controllerService.doAction(update, vm, token);
            if (vm.getErrorMessage() != null) {
                return WebUtils.sanitizeOutput(vm.getErrorMessage());
            }
        }

        return WebUtils.sanitizeOutput(errorMessage);
    }

}
