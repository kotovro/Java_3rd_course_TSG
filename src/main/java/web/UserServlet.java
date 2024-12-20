package web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.entities.ListItem;
import services.ListRouteProvider;
import services.RouteType;
import services.actionProviders.ActionProviderContainer;
import view.Action;
import view.ViewField;
import view.ViewModel;
import web.page.elements.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/User/*")
public class UserServlet extends HttpServlet {

    public static final String ROLES_ELEMENT_ID = "roles";

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
                        ActionProviderContainer.getUserActionProvider(), token, param));
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
                String error = getUserUpdateResult(req);
                if (!error.isEmpty()) {
                    out.println(error);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            }
            default:
                out.println(pathInfo + " default");
                break;
        }
    }

    private static String getUserHTML(String param, String token) {
        Action showRole = ActionProviderContainer
                .getUserActionProvider().getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showRole, null, token);

        PageContent pageContent = new PageContent();
        pageContent.getInputs().addAll(getPartHTMLViewFields(token, viewModel));
        pageContent.getSelects().addAll(getPartHTMLSelect(token, viewModel));
        pageContent.getButtons().addAll(getPartHTMLButtons(token, viewModel));
        pageContent.getMultipleSelects().addAll(getPartHTMLSMultipleSelect(token, viewModel));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(pageContent);
        } catch (Exception e) {
            return "";
        }
    }

    private static List<WebMultiSelect> getPartHTMLSMultipleSelect(String token, ViewModel viewModel) {
        List<WebMultiSelect> selectList = new LinkedList<>();
        List<ListItem> options = new LinkedList<>();
        WebMultiSelect select = null;
        for (ViewField viewField : viewModel.getParameters()) {
            if (viewField.isDisplayable() && viewField.isListMultiple()) {
                if (select == null) {
                    select = new WebMultiSelect(ROLES_ELEMENT_ID, "Roles", viewField.getDataSource()  + "?token=" + token, options);
                }
                String optionId = WebUtils.sanitizeOutput(
                        viewField.getAttributeValue());
                String optionText = WebUtils.sanitizeOutput(
                        viewField.getAttributeName());
                options.add(new ListItem(optionId, optionText));
            }
        }
        if (select != null) {
            selectList.add(select);
        }
        return selectList;
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
            if (viewField.isDisplayable() && !viewField.isList() && !viewField.isListMultiple()) {
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

    private String getUserUpdateResult(HttpServletRequest req) throws JsonProcessingException {
        String param = req.getParameter("param");
        String token = req.getParameter("token");
        Action showAction = ActionProviderContainer.getUserActionProvider().getActionShow(param, null, null, null, false);
        ControllerService controllerService = new ControllerService();
        ViewModel vm = controllerService.doAction(showAction, null, token);
        boolean isUserChanged = false;
        String errorMessage = "";
        String roles = req.getParameter(ROLES_ELEMENT_ID);
        List<String> roleIds = new LinkedList<>(List.of(roles.split(",")));
        roleIds.removeIf(e -> e.equals("-1"));

        List<ViewField> rolesToDelete = new LinkedList<>();
        for (ViewField viewField : vm.getParameters()) {
            if (viewField.isChangeable() && !viewField.isListMultiple()) {
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
            else if (viewField.isListMultiple() && viewField.isChangeable()) {
                if (!roleIds.contains(viewField.getAttributeValue())) {
                    rolesToDelete.add(viewField);
                    if (!viewField.getAttributeValue().equals("-1")) {
                        isUserChanged = true;
                    }
                }
            }
        }
        vm.getParameters().removeAll(rolesToDelete);
        for (String roleId : roleIds) {
            vm.getParameters().add(new ViewField("", roleId,
                    false, true, null, false, true, false,
                    "", null));
            isUserChanged = true;
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
