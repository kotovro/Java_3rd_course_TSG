import models.entities.*;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import services.RepositoryProvider;
import controllers.RequestController;
import view.Action;
import services.ControllerService;
import applications.IApplication;
import applications.ConsoleApplication;

import java.time.ZonedDateTime;

public class Main {
    private static void fillRepositoriesWithTestData(RepositoryProvider repProvider){

        ICommentRepository commentRepository =  repProvider.getCommentRepository();
        commentRepository.add(new Comment(1, 1, 1, "s", ZonedDateTime.now(), "NOPE"));

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
        action.setController("controllers.RequestController");
        action.setAction("getList");
        action.setParameter("");
        ControllerService controllerService = new ControllerService();
        application.start(action, controllerService);

    }
}
