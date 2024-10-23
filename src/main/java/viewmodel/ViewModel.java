package viewmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViewModel  {
    private String title;
    private List<ViewField> parameters;
    private List<Action> actionsList;
    private String errorMessage;
}
