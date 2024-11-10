import models.entities.*;
import models.repositories.interfaces.ICommentRepository;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.IUserRepository;
import services.*;
import services.actionProviders.*;
import view.Action;
import controller.ControllerService;
import applications.IApplication;
import applications.ConsoleApplication;

import java.time.ZonedDateTime;

public class Main {
    private static void fillRepositoriesWithTestData(RepositoryProvider repProvider){

        ICommentRepository commentRepository =  repProvider.getCommentRepository();
        commentRepository.add(new Comment(1, 1, 1, "s", ZonedDateTime.now(), "NOPE"));

        IUserRepository userRepository = repProvider.getUserRepository();
        userRepository.add(new User(1, "test", "HnO6qrPRkmsVkONr/TOUvQ==", new byte[]{83, 59, 110, 125, 48, 77, 18, 99, -18, -69, -67, -103, -67, 44, 124, -61}));
        IResidentRepository residentRepository = repProvider.getResidentRepository();
        residentRepository.add(new Resident(1, 1, "Saaas", "Sooos", true, "19A", "Test"));
        IRequestRepository requestRepository = repProvider.getRequestRepository();
        requestRepository.add(new Request(1, RequestType.COLLECTIVE, RequestState.STOPPED, "Somethong's wrong", 1, 1,  ZonedDateTime.now(), false));

    }

    public static void main(String[] args){

        RequestService rc = new RequestService();
        System.out.println(rc.getClass());
        System.out.println(rc.getClass());
        RepositoryProvider repoProv = RepositoryProvider.init(RepositoryProvider.RepositoryType.IN_MEMORY);
        fillRepositoriesWithTestData(repoProv);

        IApplication application = new ConsoleApplication();
//        Action action = new Action(Action.ActionType.SHOW, "Request/getList",
//                "", "Back to requests list");
        ControllerService controllerService = new ControllerService();
        IActionProvider commentActionService = new CommentActionProvider();
        IActionProvider requestActionService = new RequestActionProvider();
        IActionProvider loginActionProvider = new LoginActionProvider();
        IActionProvider userActionProvider = new UserActionProvider();
        ActionProviderContainer.init(commentActionService,
                                    requestActionService,
                                    loginActionProvider,
                                    userActionProvider);
        Action landing = loginActionProvider.getActionShow("", "", null, null, false);
//        Action login = loginActionProvider.getActionAdd("", "", landing, null);
        application.start(landing, controllerService);

    }
}
