package view;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.Permission;
import models.entities.PermissionLevel;
import models.entities.Permissions;

@Getter
@Setter
@AllArgsConstructor
public class Action {
    public enum ActionType {
        UPDATE,
        ADD,
        DELETE,
        RETURN,
        SHOW,
        EXIT,
        BACK
    }

    private ActionType actionType;
    private String route = null;
    private String parameter;
    private String actionName;
    private Action onSuccess = null;
    private Action onError = null;
    private String dataSource = null;
    private boolean isInteractive = true;
    private Permissions permissionId = null;
    private PermissionLevel permissionLevel = null;
    private boolean isListItem = false;

    public Action(ActionType actionType, String route, String parameter, String actionName) {
        this.actionType = actionType;
        this.route = route;
        this.parameter = parameter;
        this.actionName = actionName;
    }

    public Action(ActionType actionType, String route, String parameter, String actionName, Action onSuccess, Action onError, boolean isInteractive, Permissions permissionId, PermissionLevel permissionLevel) {
        this.actionType = actionType;
        this.route = route;
        this.parameter = parameter;
        this.actionName = actionName;
        this.permissionId = permissionId;
        this.permissionLevel = permissionLevel;
        this.isInteractive = isInteractive;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    public Action() {}
}
