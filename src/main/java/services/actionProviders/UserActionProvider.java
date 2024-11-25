package services.actionProviders;

import models.entities.Permission;
import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class UserActionProvider implements IActionProvider {
    @Override
    public Action getActionUpdate(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "User/update",
                param, name, onSuccess, onError,
                true, Permissions.USER, PermissionLevel.FULL_RIGHTS);
    }

    @Override
    public Action getActionShow(String requestId, String name,
                                Action onSuccess, Action onError,
                                boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "User/show",
                requestId, name, onSuccess, onError, isInteractive, Permissions.USER, PermissionLevel.READONLY);
    }

    @Override
    public Action getActionList(String requestId, String name, Action onSuccess, Action onError) {
        return  new Action(Action.ActionType.SHOW, "User/getList",
                requestId, name, onSuccess, onError, true, Permissions.USER, PermissionLevel.READONLY);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return new Action(Action.ActionType.SHOW, "User/getList",
                param, name);
    }

    @Override
    public Action getActionAdd(String requestId, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "User/add",
                requestId, name, onSuccess, onError, true, Permissions.USER, PermissionLevel.ADD_NEW);
    }

    @Override
    public Action getActionDelete(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.DELETE, "User/delete",
                param, name, null, null, true, Permissions.USER, PermissionLevel.FULL_RIGHTS);
    }
}
