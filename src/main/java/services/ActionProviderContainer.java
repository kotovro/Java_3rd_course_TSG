package services;

import lombok.Getter;

public class ActionProviderContainer {

    @Getter
    private static IActionProvider commentActionProvider;
    @Getter
    private static IActionProvider requestActionProvider;
    private static ActionProviderContainer instance;

    public static void init(IActionProvider commentActionProvider, IActionProvider requestActionProvider) {
        if (instance == null) {
            instance = new ActionProviderContainer(commentActionProvider, requestActionProvider);
        }
    }

    private ActionProviderContainer(IActionProvider commentActionProvider, IActionProvider requestActionProvider) {
        ActionProviderContainer.commentActionProvider = commentActionProvider;
        ActionProviderContainer.requestActionProvider = requestActionProvider;
    }
}
