package services.actionProviders;

import lombok.Getter;

public class ActionProviderContainer {

    @Getter
    private static IActionProvider commentActionProvider;
    @Getter
    private static IActionProvider requestActionProvider;
    @Getter
    private static IActionProvider loginActionProvider;
    @Getter
    private static IActionProvider userActionProvider;
    private static ActionProviderContainer instance;

    public static void init(IActionProvider commentActionProvider,
                            IActionProvider requestActionProvider,
                            IActionProvider loginActionProvider,
                            IActionProvider userActionProvider) {
        if (instance == null) {
            instance = new ActionProviderContainer(commentActionProvider,
                                                    requestActionProvider,
                                                    loginActionProvider,
                                                    userActionProvider);
        }
    }

    private ActionProviderContainer(IActionProvider commentActionProvider,
                                    IActionProvider requestActionProvider,
                                    IActionProvider loginActionProvider,
                                    IActionProvider userActionProvider) {
        ActionProviderContainer.commentActionProvider = commentActionProvider;
        ActionProviderContainer.requestActionProvider = requestActionProvider;
        ActionProviderContainer.loginActionProvider = loginActionProvider;
        ActionProviderContainer.userActionProvider = userActionProvider;
        ActionProviderContainer.instance = this;
    }
}
