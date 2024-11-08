package services;

import view.Action;

public class CommentActionService implements IActionProvider{


    @Override
    public Action getActionShow(String param, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "Comment/show", param, name, onSuccess, onError, isInteractive);
    }

    @Override
    public Action getActionList(String requestId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.SHOW, "Comment/getList",
                requestId, name);
    }

    @Override
    public Action getActionUpdate(String commentId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Comment/update", commentId, name, onSuccess, onError, true);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return new Action(Action.ActionType.BACK, "Comment/back", param, name);
    }

    @Override
    public Action getActionAdd(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Comment/add", param,
                name, onSuccess, onError, true);
    }
}
