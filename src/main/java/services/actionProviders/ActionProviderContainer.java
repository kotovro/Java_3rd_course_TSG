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
    @Getter
    private static IActionProvider roleActionProvider;
    @Getter
    private static IActionProvider permissionActionProvider;
    private static ActionProviderContainer instance;

    public static void init(IActionProvider commentActionProvider,
                            IActionProvider requestActionProvider,
                            IActionProvider loginActionProvider,
                            IActionProvider userActionProvider,
                            IActionProvider roleActionProvider,
                            IActionProvider permissionActionProvider) {
        if (instance == null) {
            instance = new ActionProviderContainer(commentActionProvider,
                                                    requestActionProvider,
                                                    loginActionProvider,
                                                    userActionProvider,
                                                    roleActionProvider,
                                                    permissionActionProvider);
        }
    }

    private ActionProviderContainer(IActionProvider commentActionProvider,
                                    IActionProvider requestActionProvider,
                                    IActionProvider loginActionProvider,
                                    IActionProvider userActionProvider,
                                    IActionProvider roleActionProvider,
                                    IActionProvider permissionActionProvider) {
        ActionProviderContainer.commentActionProvider = commentActionProvider;
        ActionProviderContainer.requestActionProvider = requestActionProvider;
        ActionProviderContainer.loginActionProvider = loginActionProvider;
        ActionProviderContainer.userActionProvider = userActionProvider;
        ActionProviderContainer.roleActionProvider = roleActionProvider;
        ActionProviderContainer.permissionActionProvider = permissionActionProvider;
        ActionProviderContainer.instance = this;
    }
}
