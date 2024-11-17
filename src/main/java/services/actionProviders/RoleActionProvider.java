package services.actionProviders;

import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class RoleActionProvider implements IActionProvider {

    @Override
    public Action getActionShow(String param, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "Role/show",
                param, name, onSuccess, onError, isInteractive, Permissions.ROLE, PermissionLevel.READONLY);
    }

    @Override
    public Action getActionList(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.SHOW, "Role/getList",
                param, name);
    }

    @Override
    public Action getActionUpdate(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Role/update",
                param, name, onSuccess, onError,
                true, Permissions.ROLE, PermissionLevel.READONLY);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return new Action(Action.ActionType.SHOW, "Role/getList",
                param, name);
    }

    @Override
    public Action getActionAdd(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Role/add",
                param, name, onSuccess, onError, true, Permissions.ROLE, PermissionLevel.ADD_NEW);
    }

    @Override
    public Action getActionDelete(String param, String name) {
        return new Action(Action.ActionType.DELETE, "Role/delete",
                param, name);
    }
}
