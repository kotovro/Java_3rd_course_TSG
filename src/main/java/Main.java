import models.entities.*;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.RepositoryProvider;
import services.*;
import view.Action;
import controller.ControllerService;
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
        requestRepository.add(new Request(1, RequestType.COLLECTIVE, RequestState.STOPPED, "Somethong's wrong", 1, 1,  ZonedDateTime.now(), false));

    }

    private static void fillRouting() {

//        RoutingService.registerController(RequestService.class, "");
    }

    public static void main(String[] args){

        RequestService rc = new RequestService();
        System.out.println(rc.getClass());
        System.out.println(rc.getClass());
        RepositoryProvider repoProv = RepositoryProvider.init(RepositoryProvider.RepositoryType.IN_MEMORY);
        fillRepositoriesWithTestData(repoProv);

        IApplication application = new ConsoleApplication();
        Action action = new Action(Action.ActionType.SHOW, "Request/getList",
                "", "Back to requests list");
//        ActionProviderContainer factory = new ActionProviderContainer();
        ControllerService controllerService = new ControllerService();
        IActionProvider commentActionService = new CommentActionService();
        IActionProvider requestActionService = new RequestActionService();
        ActionProviderContainer.init(commentActionService, requestActionService);
        application.start(action, controllerService);

    }
}
