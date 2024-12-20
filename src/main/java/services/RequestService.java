package services;


import lombok.Setter;
import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.entities.User;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.IUserRepository;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import services.actionProviders.PaginationActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

public class RequestService  {

    @Setter
    private RepositoryProvider repositoryProvider;


    public ViewModel fillView(String param) {
        String requestIdStr = "-1";
        String[] params = param.split(";");
        for (String kvPair: params) {
            String[] keyValue = kvPair.split(":");
            if (keyValue[0].equals("id")) {
                requestIdStr = keyValue[1];
            }
        }

        String pagingParams = param.replace("id:" + requestIdStr, "");
        int requestId = Integer.parseInt(requestIdStr);
        ViewModel requestMdlView = new ViewModel();

        Request request = null;
        String residentName = "N/A";
        String authorName = "N/A";
        if (requestId > 0) {
            requestMdlView.setTitle("Request " + Integer.toString(requestId));
            IRequestRepository requestRep = repositoryProvider.getRequestRepository();
            request = requestRep.getRequestById(requestId);
            IResidentRepository residentRep = repositoryProvider.getResidentRepository();
            residentName = residentRep.getNameById(request.getResidentId());

            IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
            authorName = staffRep.getNameByUserId(request.getAuthorId()) == null
                    ? staffRep.getNameByUserId(request.getAuthorId())
                    : residentRep.getNameByUserId(request.getAuthorId());
        }
        else {
            requestMdlView.setTitle("New request");
            request = new Request();
        }
        if (request == null) {
            return requestMdlView;
        }

        List<ViewField> parameters = requestMdlView.getParameters();
        parameters.add(new ViewField("Request Id", requestIdStr, false, false));
        parameters.add(new ViewField("Description", request.getDescription(), true, true));
        parameters.add(new ViewField("Author Id", Integer.toString(request.getAuthorId()), false, false));
        parameters.add(new ViewField("Author", authorName, false, true));

        ViewField resident = new ViewField("Resident Id", Integer.toString(request.getResidentId()), true, false);
        parameters.add(resident);
        parameters.add(new ViewField("Resident", residentName, false, true, resident, true, ListRouteProvider.getRoute(RouteType.RESIDENT)));

        ViewField type = new ViewField("Type", Integer.toString(request.getType().getRequestTypeId()), true, false);
        parameters.add(type);
        parameters.add(new ViewField("Request Type", request.getType().toString(), false, true, type, true, ListRouteProvider.getRoute(RouteType.TYPE)));

        ViewField status = new ViewField("Status", Integer.toString(request.getState().getStateId()), true, false);
        parameters.add(status);
        parameters.add(new ViewField("Request Status", request.getState().toString(), false, true, status, true, ListRouteProvider.getRoute(RouteType.STATUS)));

        parameters.add(new ViewField("Date", request.getTime().toString(), false, true));

        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();
        Action show = requestActionProvider.getActionShow(requestIdStr, "", null, null, false);
        Action back = requestActionProvider.getActionBack(pagingParams, "Back to requests list");
        Action update = requestActionProvider.getActionUpdate(requestIdStr,
                requestId > 0 ? "Update request" : "Add request", show, show);
        requestMdlView.addCommand(update);

        if (requestId > 0) {
            Action add = requestActionProvider.getActionAdd("-1", "Add", update, update);
            requestMdlView.addCommand(add);

            Action delete = requestActionProvider.getActionDelete(requestIdStr, "Delete", back, back);
            requestMdlView.addCommand(delete);

            IActionProvider commentActionProvider = ActionProviderContainer.getCommentActionProvider();
            Action showComments = commentActionProvider.getActionList(requestIdStr,"Show comments to request", null, null);
            requestMdlView.addCommand(showComments);
        }

        requestMdlView.addCommand(back);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        requestMdlView.addCommand(exit);

        return requestMdlView;
    }

    public ViewModel getList(String pageParams) {
        String[] params = pageParams.split(";");
        int pageNumber = 1;
        int pageSize = 10;
        for (String kvPair: params) {
            String[] keyValue = kvPair.split(":");
            if (keyValue[0].equals("pn")) {
                pageNumber = Integer.parseInt(keyValue[1]);
            } else if (keyValue[0].equals("ps")) {
                pageSize = Integer.parseInt(keyValue[1]);
            }
        }
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Requests list");
        IRequestRepository rep = repositoryProvider.getRequestRepository();
        List<Request> reqList = rep.getRequestList(pageNumber, pageSize);
        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();
        for (Request request : reqList)
        {
            Action action = requestActionProvider.getActionShow( Integer.toString(request.getRequestId()), "Request " + request.getRequestId() + "\n", null, null, true);
            action.setListItem(true);
            viewModel.getActionsList().add(action);
        }
        Action updateNew = requestActionProvider.getActionUpdate("-1", "", null, null);
        Action add = requestActionProvider.getActionAdd("id:-1;" + pageParams, "Add new request", updateNew, null);
        viewModel.getActionsList().add(add);
        Action exit = new Action();

        Action actionList = requestActionProvider.getActionList("", "", null, null);
        PaginationActionProvider paginationActionProvider = ActionProviderContainer.getPaginationActionProvider();
        int requestCount = rep.getRequestCount();
        viewModel.getActionsList().addAll(paginationActionProvider.getPaginationActions(actionList, requestCount, pageNumber, pageSize));

        exit.setActionType(Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    public ViewModel update(ViewModel viewModel, int userId) {
        int id = Integer.parseInt(
                viewModel.getParameters()
                        .stream()
                        .filter(p -> p.getAttributeName().equals("Request Id"))
                        .findFirst()
                        .get()
                        .getAttributeValue());
        IRequestRepository rep = repositoryProvider.getRequestRepository();
        Request req = null;
        if (id < 0)
        {
            req = new Request();
            req.setAuthorId(userId);
        } else {
            req = rep.getRequestById(id);
        }
        req.setDescription(viewModel.getFieldValueByAttributeName("Description"));
        req.setResidentId(Integer.parseInt(viewModel.getFieldValueByAttributeName("Resident Id")));
        req.setType(RequestType.name(Integer.parseInt(viewModel.getFieldValueByAttributeName("Type"))));
        req.setState(RequestState.name(Integer.parseInt(viewModel.getFieldValueByAttributeName("Status"))));
        id = rep.updateRequest(req);

        return fillView(Integer.toString(id));
    }

    public ViewModel add() {
        return fillView("-1");
    }
}

