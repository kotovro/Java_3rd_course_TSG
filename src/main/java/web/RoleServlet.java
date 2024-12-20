package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.entities.ListItem;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;
import web.page.elements.*;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static web.page.elements.ButtonType.GET;

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

                resp.setContentType("application/json");
                out.println(WebUtils.generateList(
                        ActionProviderContainer.getRoleActionProvider(), token, param));
                break;
            }
            case "/show":
            case "/add": {
                resp.setContentType("application/json");
                out.println(getRoleHTML(param, token));
                break;
            }
            case "/update": {
                String error = getRoleUpdateResult(req);
                if (!error.isEmpty()) {
                    out.println(error);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            }
            case "/delete": {
                resp.setContentType("application/json");
                deleteRole(param, token);
                out.println(WebUtils.generateList(
                        ActionProviderContainer.getRoleActionProvider(), token, param));
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

}
