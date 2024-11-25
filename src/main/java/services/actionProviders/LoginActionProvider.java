package services.actionProviders;


import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class LoginActionProvider implements IActionProvider {


    @Override
    public Action getActionShow(String param, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return new Action(Action.ActionType.SHOW, "Login/login", param, name, onSuccess, onError, isInteractive, Permissions.USER, PermissionLevel.NOT_ACCESSIBLE);
    }

    @Override
    public Action getActionList(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.SHOW, "Login/landing", param, name, onSuccess, onError, true, Permissions.USER, PermissionLevel.NOT_ACCESSIBLE);
    }

    @Override
    public Action getActionUpdate(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Login/authenticate", param, name, onSuccess, onError, true, Permissions.USER, PermissionLevel.ADD_NEW);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return null;
    }

    @Override
    public Action getActionAdd(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Login/login", param, name, onSuccess, onError, true, Permissions.USER, PermissionLevel.FULL_RIGHTS);
    }

    @Override
    public Action getActionDelete(String param, String name, Action onSuccess, Action onError) {
        return null;
    }
}
