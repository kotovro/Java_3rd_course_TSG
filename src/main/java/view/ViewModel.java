package view;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class ViewModel  {
    @Setter
    private String title;
    private List<ViewField> parameters = new LinkedList<>();
    private List<Action> actionsList = new LinkedList<>();
    @Setter
    private String errorMessage;

    public void addCommand(Action action) {
        actionsList.add(action);
    }
}
