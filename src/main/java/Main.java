import models.entities.*;
import models.repositories.interfaces.*;
import models.repositories.RepositoryProvider;
import services.*;
import services.actionProviders.*;
import view.Action;
import controller.ControllerService;
import applications.IApplication;
import applications.ConsoleApplication;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static void fillRepositoriesWithTestData(RepositoryProvider repProvider){

        ICommentRepository commentRepository =  repProvider.getCommentRepository();
        commentRepository.add(new Comment(1, 1, 1, "s", ZonedDateTime.now(), "NOPE"));

        IRoleRepository roleRepository =  repProvider.getRoleRepository();
        Permission p = new Permission();
        p.setId(Permissions.USER);
        p.setLevel(PermissionLevel.NOT_ACCESSIBLE);
        Permission p1 = new Permission();
        p1.setId(Permissions.COMMENT);
        p1.setLevel(PermissionLevel.NOT_ACCESSIBLE);
        Permission p2 = new Permission();
        p2.setId(Permissions.REQUEST);
        p2.setLevel(PermissionLevel.NOT_ACCESSIBLE);
        Permission p3 = new Permission();
        p3.setId(Permissions.ROLE);
        p3.setLevel(PermissionLevel.NOT_ACCESSIBLE);

        List<Permission> permissions = new LinkedList<>();
        permissions.add(p);
        permissions.add(p1);
        permissions.add(p2);
        permissions.add(p3);

        Permission p6 = new Permission();
        p6.setId(Permissions.USER);
        p6.setLevel(PermissionLevel.FULL_RIGHTS);
        Permission p7 = new Permission();
        p7.setId(Permissions.COMMENT);
        p7.setLevel(PermissionLevel.FULL_RIGHTS);
        Permission p8 = new Permission();
        p8.setId(Permissions.REQUEST);
        p8.setLevel(PermissionLevel.FULL_RIGHTS);
        Permission p9 = new Permission();
        p9.setId(Permissions.ROLE);
        p9.setLevel(PermissionLevel.FULL_RIGHTS);

        List<Permission> permissions1 = new LinkedList<>();
        permissions1.add(p6);
        permissions1.add(p7);
        permissions1.add(p8);
        permissions1.add(p9);
        LinkedList<Role> roles = new LinkedList<>();
        roles.add(new Role(0, "Test", permissions));
        roles.add(new Role(1, "Test1", permissions1));
        roleRepository.addRole(new Role(0, "Test", permissions));
        roleRepository.addRole(new Role(1, "Test1", permissions1));

        LinkedList<Integer> rolesIds = new LinkedList<>();
        rolesIds.add(0);
        rolesIds.add(1);

        IUserRepository userRepository = repProvider.getUserRepository();
        userRepository.add(new User(1, "test", "HnO6qrPRkmsVkONr/TOUvQ==", new byte[]{83, 59, 110, 125, 48, 77, 18, 99, -18, -69, -67, -103, -67, 44, 124, -61}, rolesIds));
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
        ControllerService controllerService = new ControllerService();
        IActionProvider commentActionService = new CommentActionProvider();
        IActionProvider requestActionService = new RequestActionProvider();
        IActionProvider loginActionProvider = new LoginActionProvider();
        IActionProvider userActionProvider = new UserActionProvider();
        IActionProvider roleActionProvider = new RoleActionProvider();
        IActionProvider permissionActionProvider = new PermissionActionProvider();
        ActionProviderContainer.init(commentActionService,
                                    requestActionService,
                                    loginActionProvider,
                                    userActionProvider,
                                    roleActionProvider,
                                    permissionActionProvider);
        Action landing = loginActionProvider.getActionShow("", "", null, null, false);
//        Action login = loginActionProvider.getActionAdd("", "", landing, null);
        application.start(landing, controllerService);

    }
}
