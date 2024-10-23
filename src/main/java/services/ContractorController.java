package services;

import lombok.AllArgsConstructor;
import models.repositories.interfaces.IContractorRepository;
import viewmodel.Action;
import viewmodel.ViewModel;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class ContractorController {
    private IContractorRepository contractorRep;

//    public ContractorController(RepositoryProvider repProvider){
//    }


    public ViewModel fillView(int contractorId)
    {
        ViewModel view =  new ViewModel();
//        Contractor contractor = contractorRep.getContractorById(contractorId);
//        Resident resident = null;
//        if (request.getResidentId() > 0){
//            resident = residentRep.getResidentById(request.getResidentId());
//        }
//        String name = staffRep.getNameByUserId(request.getAuthorId()) == null
//                ? staffRep.getNameByUserId(request.getAuthorId())
//                : residentRep.getNameByUserId(request.getAuthorId());
//
//        view.setTitle("Request");
//
//        List<ViewField> parameters = new LinkedList<>();
//        parameters.add(new ViewField("Description", request.getDescription()));
//        parameters.add(new ViewField("Author", name));
//        parameters.add(new ViewField("Resident", resident.getName() + " " + resident.getSurname()));
//        parameters.add(new ViewField("Type", request.getType()));
//        parameters.add(new ViewField("Status", request.getState()));
//        parameters.add(new ViewField("Date", request.getTime().toString()));
//        view.setParameters(parameters);
//
//        List<Action> commands = new LinkedList<>();
//        Action update = new Action();
//        update.setActionType(Action.ActionType.UPDATE);
//        commands.add(update);
//        Action add = new Action();
//        add.setActionType(Action.ActionType.ADD);
//        Action delete = new Action();
//        commands.add(add);
//        delete.setActionType(Action.ActionType.DELETE);
//        Action exit = new Action();
//        commands.add(delete);
//        exit.setActionType(Action.ActionType.EXIT);
//        commands.add(exit);
//        requestMdlView.setActionsList(commands);
//
        return view;
    }
}
