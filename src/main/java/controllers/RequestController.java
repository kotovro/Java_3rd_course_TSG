package controllers;


import lombok.Setter;
import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import services.RepositoryProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

@RequestService
public class RequestController implements IController {

    @Setter
    RepositoryProvider repositoryProvider;


//    public Re


    @ShowRequest
    public ViewModel fillView(String requestIdStr) {
        int requestId = Integer.parseInt(requestIdStr);
        ViewModel requestMdlView = new ViewModel();

        if (requestId > 0) {
            requestMdlView.setTitle("Request" + Integer.toString(requestId));
        }
        else {
            requestMdlView.setTitle("New request");
        }
        IRequestRepository requestRep = repositoryProvider.getRequestRepository();
        Request request = requestRep.getRequestById(requestId);
        if (request == null) {
            return requestMdlView;
        }
        IResidentRepository residentRep = repositoryProvider.getResidentRepository();
        String residentName = residentRep.getNameById(request.getResidentId());

        IStaffMemberRepository staffRep = repositoryProvider.getStaffMemberRepository();
        String authorName = staffRep.getNameByUserId(request.getAuthorId()) == null
                ? staffRep.getNameByUserId(request.getAuthorId())
                : residentRep.getNameByUserId(request.getAuthorId());

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


        Action show = new Action();

        Action update = new Action();
        update.setActionType(Action.ActionType.UPDATE);
        update.setController("controllers.RequestController");
        update.setAction("update");
        update.setOnSuccess(show);
        update.setOnError(show);
        update.setParameter(Integer.toString(requestId));
        requestMdlView.addCommand(update);

        Action add = new Action();
        add.setActionType(Action.ActionType.ADD);
        add.setController("controllers.RequestController");
        add.setAction("update");
        add.setParameter("-1");
        requestMdlView.addCommand(add);

        Action delete = new Action();
        delete.setActionType(Action.ActionType.DELETE);
        requestMdlView.addCommand(delete);

        Action showComments = new Action();
        showComments.setActionName("Show comments to request");
        showComments.setActionType(Action.ActionType.SHOW);
        showComments.setParameter(requestIdStr);
        showComments.setController("controllers.CommentController");
        showComments.setAction("getList");
        requestMdlView.addCommand(showComments);

        Action back = new Action();
        back.setActionName("Back to requests list");
        back.setActionType(Action.ActionType.SHOW);
        back.setController("controllers.RequestController");
        back.setParameter("");
        back.setAction("getList");
        requestMdlView.addCommand(back);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        requestMdlView.addCommand(exit);

        show.setActionType(Action.ActionType.SHOW);
        show.setController("controllers.RequestController");
        show.setParameter(Integer.toString(requestId));
        show.setAction("fillView");
        show.setInteractive(false);
        requestMdlView.addCommand(show);

        return requestMdlView;
    }

    public ViewModel getList(String param) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Requests list");
        IRequestRepository rep = repositoryProvider.getRequestRepository();
        List<Request> reqList = rep.getRequestList();
        for (Request request : reqList)
        {
            Action action = new Action();
            action.setActionName("Request " + request.getRequestId() + "\n");
            action.setActionType(Action.ActionType.SHOW);
            action.setController("controllers.RequestController");
            action.setAction("fillView");
            action.setParameter(Integer.toString(request.getRequestId()));
            viewModel.getActionsList().add(action);
        }
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
        Request req = rep.getRequestById(id);
        req.setDescription(viewModel.getParameters()
                .stream()
                .filter(f -> f.getAttributeName().equals("Description"))
                .findFirst()
                .get()
                .getAttributeValue());
        req.setAuthorId(Integer.parseInt(
                viewModel.getParameters()
                .stream()
                .filter(f -> f.getAttributeName().equals("Author Id"))
                .findFirst()
                .get()
                .getAttributeValue()));
        req.setResidentId(Integer.parseInt(
                viewModel.getParameters()
                        .stream()
                        .filter(f -> f.getAttributeName().equals("Resident Id"))
                        .findFirst()
                        .get()
                        .getAttributeValue()));

        req.setType(RequestType.valueOf(
                viewModel.getParameters()
                        .stream()
                        .filter(f -> f.getAttributeName().equals("Type"))
                        .findFirst()
                        .get()
                        .getAttributeValue()));
        req.setState(RequestState.valueOf(
                viewModel.getParameters()
                        .stream()
                        .filter(f -> f.getAttributeName().equals("Status"))
                        .findFirst()
                        .get()
                        .getAttributeValue()));
        req.setType(RequestType.valueOf(
                viewModel.getParameters()
                        .stream()
                        .filter(f -> f.getAttributeName().equals("Type"))
                        .findFirst()
                        .get()
                        .getAttributeValue()));
        rep.updateRequest(req);
        return viewModel;
    }
}

