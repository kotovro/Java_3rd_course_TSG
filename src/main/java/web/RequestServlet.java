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
import java.util.LinkedList;
import java.util.List;

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
                resp.setContentType("application/json");
                out.println(getRequestListHTML(token));
                break;
            }
            case "/show":
            case "/add":
            {
                resp.setContentType("application/json");
                out.println(getRequestHTML(param, token));
                break;
            }
            case "/update":
            {
                out.println(getRequestUpdateResult(req));
                break;
            }case "/delete": {
                resp.setContentType("application/json");
                deleteRequest(param, token);
                out.println(getRequestListHTML(token));
                break;
            }

            default:
                out.println(pathInfo + " default");
                break;
        }
    }

    private void deleteRequest(String param, String token) {
        Action deleteRequest = ActionProviderContainer
                .getRequestActionProvider().getActionDelete(param, "", null, null);
        ControllerService controllerService = new ControllerService();
        controllerService.doAction(deleteRequest, null, token);

    }

    private static String getRequestHTML(String param, String token) {
        Action showRequest = ActionProviderContainer
                .getRequestActionProvider()
                .getActionShow(param, "", null, null, true);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(showRequest, null, token);
        PageContent pageContent = new PageContent();
        pageContent.getButtons().addAll(getButtonsHTML(token, viewModel));
        pageContent.getSelects().addAll(getSelectsHTML(token, viewModel));
        pageContent.getInputs().addAll(getInputsHTML(token, viewModel));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(pageContent);
        } catch (Exception e) {
            return "";
        }
    }

    private static List<WebInput> getInputsHTML(String token, ViewModel viewModel) {
        List<WebInput> inputs = new LinkedList<>();
        for (ViewField viewField : viewModel.getParameters()) {
            if (!viewField.isList() && viewField.isDisplayable()) {
                String fieldId = WebUtils.sanitizeOutput(viewField.getAttributeName().replace(' ', '_'));
                inputs.add(new WebInput(fieldId,
                        WebUtils.sanitizeOutput(viewField.getAttributeName()),
                        WebUtils.sanitizeOutput(viewField.getAttributeValue()),
                        viewField.isChangeable()));
            }
        }
        return inputs;
    }


    private static List<WebButton> getButtonsHTML(String token, ViewModel viewModel) {
        List<WebButton> webButtons = new LinkedList<>();
        for (Action action: viewModel.getActionsList())
        {
            if (action.getActionName() != null && !action.isListItem() && action.isInteractive()){
                webButtons.add(new WebButton("",
                            WebUtils.sanitizeOutput(action.getActionName()),
                            action.getRoute()
                                    + "?token=" + token
                                    + "&param=" + action.getParameter(),
                            action.getActionType().equals(Action.ActionType.UPDATE)
                                ? ButtonType.UPDATE
                                : ButtonType.GET));
            }
        }
        return webButtons;
    }

    private static  List<WebSelect> getSelectsHTML(String token, ViewModel viewModel) {
        List<WebSelect> webSelects = new LinkedList<>();

        for (ViewField viewField: viewModel.getParameters()) {
            if (viewField.isList() && viewField.isDisplayable()) {
                webSelects.add(new WebSelect(
                        WebUtils.sanitizeOutput(viewField.getValueFrom().getAttributeName().replace(' ', '_')),
                        WebUtils.sanitizeOutput(viewField.getAttributeName()),
                        WebUtils.sanitizeOutput(viewField.getValueFrom().getAttributeValue()),
                        WebUtils.sanitizeOutput(viewField.getAttributeValue()),
                        viewField.getDataSource() + "?token=" + token));
            }
        }
        return webSelects;
    }

    private String getRequestListHTML(String token) {
        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();

        Action getList = requestActionProvider.getActionList(null, null, null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(getList, null, token);

        PageContent pageContent = new PageContent();
        List<WebListItem> listItems = pageContent.getListItems();
        List<WebButton> listButtons = pageContent.getButtons();

        for (Action action: viewModel.getActionsList()) {
            if (action.isListItem()) {
                listItems.add(new WebListItem("",
                        action.getRoute() +
                                "?token=" + token +
                                "&param=" + action.getParameter(),
                        WebUtils.sanitizeOutput(action.getActionName())));
            } else if (action.isInteractive() && action.getActionName() != null) {
                listButtons.add(new WebButton("",
                        WebUtils.sanitizeOutput(action.getActionName()),
                        action.getRoute() +
                                "?token=" + token +
                                "&param=" + action.getParameter(),
                        action.getActionType().equals(Action.ActionType.UPDATE)
                                ? ButtonType.UPDATE
                                : ButtonType.GET
                ));
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(pageContent);
        } catch (Exception e) {
            return "";
        }
    }
    private String getRequestUpdateResult(HttpServletRequest req) {
        String param = req.getParameter("param");
        String token = req.getParameter("token");
        Action showAction = ActionProviderContainer.getRequestActionProvider().getActionShow(param, null, null, null, false);
        ControllerService controllerService = new ControllerService();
        ViewModel vm = controllerService.doAction(showAction, null, token);
        boolean isRequestChanged = false;
        String errorMessage = "";

        for (ViewField viewField : vm.getParameters()) {
            if (viewField.isChangeable()) {
                String fieldId = WebUtils.sanitizeOutput(
                        viewField.isList()
                                ? viewField.getValueFrom().getAttributeName().replace(' ', '_')
                                : viewField.getAttributeName().replace(' ', '_'));
                String value = req.getParameter(fieldId);

                if (value != null && !value.equals(WebUtils.sanitizeOutput(viewField.getAttributeValue()))) {
                    viewField.setAttributeValue(value);
                    isRequestChanged = true;
                }
            }
        }

        Action update = vm.getActionsList().stream()
                .filter(a -> a.getActionType() == Action.ActionType.UPDATE)
                .findFirst()
                .orElse(null);

        if (isRequestChanged && update != null) {
            vm = controllerService.doAction(update, vm, token);
            if (vm.getErrorMessage() != null) {
                return WebUtils.sanitizeOutput(vm.getErrorMessage());
            }
        }
        return WebUtils.sanitizeOutput(errorMessage);
    }

}
