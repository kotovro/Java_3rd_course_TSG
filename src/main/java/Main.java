import models.entities.*;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import services.RepositoryProvider;
import services.RequestController;
import viewmodel.Action;
import viewmodel.ControllerService;
import viewmodel.IApplication;
import viewmodel.ViewModel;
import viewmodel.console.ConsoleApplication;

import java.time.ZonedDateTime;

public class Main {
    private static void fillRepositoriesWithTestData(RepositoryProvider repProvider){

        ICommentRepository commentRepository =  repProvider.getCommentRepository();
        IResidentRepository residentRepository = repProvider.getResidentRepository();
        residentRepository.add(new Resident(1, 1, "Saaas", "Sooos", true, "19A", "Test"));
        IRequestRepository requestRepository = repProvider.getRequestRepository();
        requestRepository.add(new Request(1, RequestType.COLLECTIVE, RequestState.STOPPED, "Somethong's wrong", 1, 1,  ZonedDateTime.now()));

    }

    public static void main(String[] args){

        RequestController rc = new RequestController();
        System.out.println(rc.getClass());
        System.out.println(rc.getClass());
        RepositoryProvider repoProv = RepositoryProvider.init(RepositoryProvider.RepositoryType.IN_MEMORY);
        fillRepositoriesWithTestData(repoProv);

        IApplication application = new ConsoleApplication();
        Action action = new Action();
        action.setActionType(Action.ActionType.SHOW);
        action.setController("services.RequestController");
        action.setAction("fillView");
        action.setParameter("1");
        ControllerService controllerService = new ControllerService();
        application.start(action, controllerService);

    }
}
