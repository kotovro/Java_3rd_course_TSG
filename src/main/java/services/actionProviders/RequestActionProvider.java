package services.actionProviders;

import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class RequestActionProvider implements IActionProvider {

    @Override
    public Action getActionUpdate(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Request/update",
                param, name, onSuccess, onError,
                true, Permissions.REQUEST, PermissionLevel.NOT_ACCESSIBLE);
    }

    @Override
    public Action getActionShow(String requestId, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "Request/show",
                requestId, name, onSuccess, onError, isInteractive, Permissions.REQUEST, PermissionLevel.NOT_ACCESSIBLE);
    }

    @Override
    public Action getActionList(String requestId, String name, Action onSuccess, Action onError) {
        return  new Action(Action.ActionType.SHOW, "Request/getList",
                requestId, name);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return new Action(Action.ActionType.SHOW, "Request/getList",
                param, name);
    }

    @Override
    public Action getActionAdd(String requestId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Request/add",
                requestId, name, onSuccess, onError, true, Permissions.REQUEST, PermissionLevel.ADD_NEW);
    }

    @Override
    public Action getActionDelete(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.DELETE, "Request/delete", param, name, onSuccess, onError, false, Permissions.REQUEST, PermissionLevel.NOT_ACCESSIBLE);
    }
}
