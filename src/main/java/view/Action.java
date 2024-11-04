package view;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    private boolean isInteractive = true;

    public Action(ActionType actionType, String route, String parameter, String actionName) {
        this.actionType = actionType;
        this.route = route;
        this.parameter = parameter;
        this.actionName = actionName;
    }
    public Action() {}
}
