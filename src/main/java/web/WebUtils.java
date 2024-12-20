package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ControllerService;
import services.actionProviders.IActionProvider;
import view.Action;
import view.PaginationControls;
import view.ViewField;
import view.ViewModel;
import web.page.elements.PageContent;
import web.page.elements.PaginationInfo;
import web.page.elements.WebButton;
import web.page.elements.WebListItem;

import java.util.List;

import static web.page.elements.ButtonType.GET;

public class WebUtils {
    public static String stringifyContent(PageContent content) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(content);
        } catch (Exception e) {
            return "";
        }
    }

    public static String sanitizeOutput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replace("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public static String generateList(IActionProvider actionProvider, String token, String param){
        Action getList = actionProvider.getActionList(param, null, null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(getList, null, token);

        PageContent pageContent = new PageContent();
        List<WebListItem> listItems = pageContent.getListItems();
        List<WebButton> listButtons = pageContent.getButtons();

        String pageNumber = "";
        String pageSize = "";
        String firstPage = "";
        String lastPage = "";
        String nextPage = "";
        String prevPage = "";

        String[] params = param.split(";");
        for (String kvPair: params) {
            String[] keyValue = kvPair.split(":");
            if (keyValue[0].equals("pn")) {
                pageNumber = keyValue[1];
            } else if (keyValue[0].equals("ps")) {
                pageSize = keyValue[1];
            }
        }
        for (Action action : viewModel.getActionsList()) {
            if (action.isListItem()) {
                listItems.add(new WebListItem("",
                        WebUtils.sanitizeOutput(action.getRoute()) +
                                "?token=" + token +
                                "&param=id:" + WebUtils.sanitizeOutput(action.getParameter()) + ";" + param,
                        WebUtils.sanitizeOutput(action.getActionName())));
            } else if (action.isInteractive() && action.getActionName() != null && action.getActionType() != Action.ActionType.PAGINATION) {
                listButtons.add(new WebButton("",
                        WebUtils.sanitizeOutput(action.getActionName()),
                        WebUtils.sanitizeOutput(action.getRoute()) +
                                "?token=" + token +
                                "&param=" + WebUtils.sanitizeOutput(action.getParameter()),
                        GET));
            } else if (action.getActionType() == Action.ActionType.PAGINATION) {
                String route = action.getRoute() +
                        "?token=" + token +
                        "&param=" + WebUtils.sanitizeOutput(action.getParameter());
                switch (PaginationControls.name(action.getActionName())) {
                    case PREVIOUS:
                        prevPage = route;
                        break;
                    case NEXT:
                        nextPage = route;
                        break;
                    case LAST:
                        lastPage = route;
                        break;
                    case FIRST:
                        firstPage = route;
                        break;
                }
            }
        }


        pageContent.setPaginationInfo(new PaginationInfo(firstPage, prevPage, pageNumber, pageSize, nextPage, lastPage));
        return stringifyContent(pageContent);
    }
}
