package services;


import lombok.Setter;
import models.entities.Request;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import viewmodel.Action;
import viewmodel.ViewField;
import viewmodel.ViewModel;

import java.util.LinkedList;
import java.util.List;

public class RequestController implements  IController {


    @Setter
    RepositoryProvider repositoryProvider;


//    public Re


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



        List<ViewField> parameters = new LinkedList<>();
        parameters.add(new ViewField("Description", request.getDescription(), true, true));
        parameters.add(new ViewField("Author Id", Integer.toString(request.getAuthorId()), true, false));
        parameters.add(new ViewField("Author", authorName, false, true));
        parameters.add(new ViewField("Resident Id", Integer.toString(request.getResidentId()), true, false));
        parameters.add(new ViewField("Resident", residentName, false, true));
        parameters.add(new ViewField("Type", request.getType().toString(), true, true));
        parameters.add(new ViewField("Status", request.getState().toString(), true, true));
        parameters.add(new ViewField("Date", request.getTime().toString(), true, true));
        requestMdlView.setParameters(parameters);

        List<Action> commands = new LinkedList<>();
        Action update = new Action();
        update.setActionType(Action.ActionType.UPDATE);
        update.setController("RequestController");
        update.setAction("update");
        update.setParameter(Integer.toString(requestId));
        commands.add(update);
        Action add = new Action();
        add.setActionType(Action.ActionType.ADD);
        update.setController("RequestController");
        update.setAction("update");
        update.setParameter("-1");
        Action delete = new Action();
        commands.add(add);
        delete.setActionType(Action.ActionType.DELETE);
        Action exit = new Action();
        commands.add(delete);
        exit.setActionType(Action.ActionType.EXIT);
        commands.add(exit);
        requestMdlView.setActionsList(commands);
        return requestMdlView;
    }
}

