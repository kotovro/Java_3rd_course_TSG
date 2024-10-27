package services;


import lombok.Setter;
import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import controller.RepositoryProvider;
import view.ViewField;
import view.ViewModel;

import java.util.List;

@Controller(name = "request")
public class RequestService implements IService {

    @Setter
    RepositoryProvider repositoryProvider;


//    public Re


    @Action(name = "show")
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


        view.Action show = new view.Action();

        view.Action update = new view.Action();
        update.setActionType(view.Action.ActionType.UPDATE);
        update.setRoute("services.RequestService/update");
        update.setOnSuccess(show);
        update.setOnError(show);
        update.setParameter(Integer.toString(requestId));
        requestMdlView.addCommand(update);

        view.Action add = new view.Action();
        add.setActionType(view.Action.ActionType.UPDATE);
        add.setRoute("services.RequestService/update");
        add.setActionName("Add new");
        add.setParameter("-1");
        requestMdlView.addCommand(add);

        view.Action delete = new view.Action();
        delete.setActionType(view.Action.ActionType.DELETE);
//        delete.setOnSuccess("");
        requestMdlView.addCommand(delete);

        view.Action showComments = new view.Action();
        showComments.setActionName("Show comments to request");
        showComments.setActionType(view.Action.ActionType.SHOW);
        showComments.setParameter(requestIdStr);
        showComments.setRoute("services.CommentService/getList");
        requestMdlView.addCommand(showComments);

        view.Action back = new view.Action();
        back.setActionName("Back to requests list");
        back.setActionType(view.Action.ActionType.SHOW);
        back.setRoute("services.RequestService/getList");
        back.setParameter("");
        requestMdlView.addCommand(back);

        view.Action exit = new view.Action();
        exit.setActionType(view.Action.ActionType.EXIT);
        requestMdlView.addCommand(exit);

        show.setActionType(view.Action.ActionType.SHOW);
        show.setRoute("services.RequestService/fillView");
        show.setParameter(Integer.toString(requestId));
        show.setInteractive(false);
        requestMdlView.addCommand(show);

        view.Action addComment = new view.Action();
        addComment.setActionName("Add comment to request");
        addComment.setActionType(view.Action.ActionType.UPDATE);
        addComment.setParameter("-1");
        addComment.setRoute("services.CommentService/update");
        requestMdlView.addCommand(addComment);

        return requestMdlView;
    }

    @Action(name = "showAll")
    public ViewModel getList(String param) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Requests list");
        IRequestRepository rep = repositoryProvider.getRequestRepository();
        List<Request> reqList = rep.getRequestList();
        for (Request request : reqList)
        {
            view.Action action = new view.Action();
            action.setActionName("Request " + request.getRequestId() + "\n");
            action.setActionType(view.Action.ActionType.SHOW);
            action.setRoute("services.RequestService/fillView");
            action.setParameter(Integer.toString(request.getRequestId()));
            viewModel.getActionsList().add(action);
        }
        view.Action exit = new view.Action();
        exit.setActionType(view.Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    @Action(name = "update")
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

