package services;

import lombok.AllArgsConstructor;
import models.repositories.interfaces.IContractorRepository;
import view.ViewModel;

@AllArgsConstructor
public class ContractorService {
    private IContractorRepository contractorRep;

//    public ContractorService(RepositoryProvider repProvider){
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
//        List<ControllerAction> commands = new LinkedList<>();
//        ControllerAction update = new ControllerAction();
//        update.setActionType(ControllerAction.ActionType.UPDATE);
//        commands.add(update);
//        ControllerAction add = new ControllerAction();
//        add.setActionType(ControllerAction.ActionType.ADD);
//        ControllerAction delete = new ControllerAction();
//        commands.add(add);
//        delete.setActionType(ControllerAction.ActionType.DELETE);
//        ControllerAction exit = new ControllerAction();
//        commands.add(delete);
//        exit.setActionType(ControllerAction.ActionType.EXIT);
//        commands.add(exit);
//        requestMdlView.setActionsList(commands);
//
        return view;
    }
}
