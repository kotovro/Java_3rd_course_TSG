package services.actionProviders;

import com.sun.net.httpserver.Authenticator;
import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;

public class PermissionActionProvider implements IActionProvider {


    @Override
    public Action getActionUpdate(String param, String name,Action onSuccess, Action onError) {
        return new Action(Action.ActionType.UPDATE, "Role/updatePermission", param, name);
    }

    @Override
    public Action getActionList(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.SHOW, "Role/listPermission", param, name);
    }

    @Override
    public Action getActionAdd(String param, String name, Action onSuccess, Action onError) {
        return new Action(Action.ActionType.ADD, "Role/editPermission", param, name,
                onSuccess, onError, true, Permissions.USER, PermissionLevel.FULL_RIGHTS);
    }

    @Override
    public Action getActionBack(String param, String name) {
        return null;
    }

    @Override
    public Action getActionShow(String param, String name, Action onSuccess, Action onError, boolean isInteractive) {
        return null;
    }

    @Override
    public Action getActionDelete(String param, String name, Action onSuccess, Action onError) {
        return null;
    }
}
