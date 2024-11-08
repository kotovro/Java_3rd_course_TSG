package services;

public class ActionProviderFactory {

    public static IActionProvider getStandardRequestActionProviders() {
            return new RequestActionService();
    }
    public static IActionProvider getStandardCommentActionProviders() {
        return new CommentActionService();
    }
}
