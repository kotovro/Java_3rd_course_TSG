package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ControllerService;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewModel;
import web.page.elements.PageContent;
import web.page.elements.WebButton;
import web.page.elements.WebListItem;

import java.util.List;

import static web.page.elements.ButtonType.GET;

public class WebUtils {
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

    public static String generateList(IActionProvider actionProvider, String token){
        Action getList = actionProvider.getActionList(null, null, null, null);
        ControllerService controllerService = new ControllerService();
        ViewModel viewModel = controllerService.doAction(getList, null, token);

        PageContent pageContent = new PageContent();
        List<WebListItem> listItems = pageContent.getListItems();
        List<WebButton> listButtons = pageContent.getButtons();

        for (Action action : viewModel.getActionsList()) {
            if (action.isListItem()) {
                listItems.add(new WebListItem("",
                        WebUtils.sanitizeOutput(action.getRoute()) +
                                "?token=" + token +
                                "&param=" + WebUtils.sanitizeOutput(action.getParameter()),
                        WebUtils.sanitizeOutput(action.getActionName())));
            } else if (action.isInteractive() && action.getActionName() != null) {
                listButtons.add(new WebButton("",
                        WebUtils.sanitizeOutput(action.getActionName()),
                        WebUtils.sanitizeOutput(action.getRoute()) +
                                "?token=" + token +
                                "&param=" + WebUtils.sanitizeOutput(action.getParameter()),
                        GET));
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(pageContent);
        } catch (Exception e) {
            return "";
        }
    }
}
