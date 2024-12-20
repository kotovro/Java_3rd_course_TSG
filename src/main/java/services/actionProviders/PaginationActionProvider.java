package services.actionProviders;

import models.entities.PermissionLevel;
import models.entities.Permissions;
import view.Action;
import view.PaginationControls;

import java.util.LinkedList;
import java.util.List;

public class PaginationActionProvider {
    public List<Action> getPaginationActions(Action action, int count, int currentPage, int pageRows) {
        List<Action> actions = new LinkedList<>();
        int firstPage = 1;
        int lastPage = count / pageRows + (count % pageRows == 0 ? 0 : 1);
        int nextPage = lastPage > currentPage ? currentPage + 1 : currentPage;
        int prevPage = firstPage < currentPage ? currentPage - 1 : currentPage;
        String parameter = "ps:" + pageRows + ";pn:";
        String route = action.getRoute();
        Action firstAction = new Action(Action.ActionType.PAGINATION, route,
                parameter + firstPage,
                PaginationControls.FIRST.getPaginationControlName());
        actions.add(firstAction);
        Action previousAction = new Action(Action.ActionType.PAGINATION, route,
                parameter + prevPage,
                PaginationControls.PREVIOUS.getPaginationControlName());
        actions.add(previousAction);
        Action nextAction = new Action(Action.ActionType.PAGINATION, route,
                    parameter + nextPage, PaginationControls.NEXT.getPaginationControlName());
        actions.add(nextAction);
        Action lastAction = new Action(Action.ActionType.PAGINATION, route,
                parameter + lastPage, PaginationControls.LAST.getPaginationControlName());
        actions.add(lastAction);
        return actions;
    }
}
