package services.actionProviders;

import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class CommentActionProvider implements IActionProvider {


    @Override
    public Action getActionShow(String param, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "Comment/show", param, name, onSuccess, onError, isInteractive, Permissions.COMMENT, PermissionLevel.NOT_ACCESSIBLE);
    }

    @Override
    public Action getActionList(String requestId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.SHOW, "Comment/getList",
                requestId, name);
    }

    @Override
    public Action getActionUpdate(String commentId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Comment/update", commentId, name, onSuccess, onError, true, Permissions.COMMENT, PermissionLevel.READONLY);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return new Action(Action.ActionType.BACK, "Comment/back", param, name);
    }

    @Override
    public Action getActionAdd(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Comment/add", param,
                name, onSuccess, onError, true, Permissions.COMMENT, PermissionLevel.ADD_NEW);
    }

    @Override
    public Action getActionDelete(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.DELETE, "Comment/delete",
                param, name, onSuccess, onError, true, Permissions.COMMENT, PermissionLevel.ADD_NEW);
    }
}
