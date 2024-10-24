package view;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private String controller;
    private String action;
    private String parameter;
    private boolean isInteractive = true;
    private String actionName;
}
