package services.actionProviders;

import models.entities.Permission;
import view.Action;

public interface IActionProvider {
    Action getActionShow(String param, String name, view.Action onSuccess, view.Action onError, boolean isInteractive);
    Action getActionList(String param, String name, view.Action onSuccess, view.Action onError);
    Action getActionUpdate(String param, String name, view.Action onSuccess, view.Action onError);
    Action getActionBack(String param, String name);
    Action getActionAdd(String param, String name, view.Action onSuccess, view.Action onError);
    Action getActionDelete(String param, String name);
}
