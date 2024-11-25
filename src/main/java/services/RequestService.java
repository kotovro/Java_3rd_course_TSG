package services;


import lombok.Setter;
import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import models.repositories.RepositoryProvider;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

public class RequestService  {

    @Setter
    private RepositoryProvider repositoryProvider;


    public ViewModel fillView(String requestIdStr) {
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
        parameters.add(new ViewField("Author Id", Integer.toString(request.getAuthorId()), true, false));
        parameters.add(new ViewField("Author", authorName, false, true));
        parameters.add(new ViewField("Resident Id", Integer.toString(request.getResidentId()), true, false));
        parameters.add(new ViewField("Resident", residentName, false, true));
        parameters.add(new ViewField("Type", request.getType().toString(), true, true));
        parameters.add(new ViewField("Status", request.getState().toString(), true, true));
        parameters.add(new ViewField("Date", request.getTime().toString(), false, true));

        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();
        Action show = requestActionProvider.getActionShow(requestIdStr, "", null, null, false);
        Action back = requestActionProvider.getActionBack("", "Back to requests list");
        Action update = requestActionProvider.getActionUpdate(requestIdStr, "Update request", show, show);
        requestMdlView.addCommand(update);

        if (requestId > 0) {
            Action add = requestActionProvider.getActionAdd("", "Add", update, update);
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

    public ViewModel getList() {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Requests list");
        IRequestRepository rep = repositoryProvider.getRequestRepository();
        List<Request> reqList = rep.getRequestList();
        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();
        for (Request request : reqList)
        {
            Action action = requestActionProvider.getActionShow( Integer.toString(request.getRequestId()), "Request " + request.getRequestId() + "\n", null, null, true);
            viewModel.getActionsList().add(action);
        }
        Action updateNew = requestActionProvider.getActionUpdate("-1", "", null, null);
        Action add = requestActionProvider.getActionAdd("-1", "Add new request", updateNew, null);
        //add action backToMainMenu
        viewModel.getActionsList().add(add);
        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    public ViewModel update(ViewModel viewModel) {
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
        } else {
            req = rep.getRequestById(id);
        }
        req.setDescription(viewModel.getFieldValueByAttributeName("Description"));
        req.setAuthorId(Integer.parseInt(viewModel.getFieldValueByAttributeName("Author Id")));
        req.setResidentId(Integer.parseInt(viewModel.getFieldValueByAttributeName("Resident Id")));
        req.setType(RequestType.valueOf(viewModel.getFieldValueByAttributeName("Type")));
        req.setState(RequestState.valueOf(viewModel.getFieldValueByAttributeName("Status")));
        id = rep.updateRequest(req);

        return fillView(Integer.toString(id));
    }

    public ViewModel add() {
        return fillView("-1");
    }
}

