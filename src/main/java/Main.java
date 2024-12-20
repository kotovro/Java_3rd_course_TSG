import applications.ConsoleApplication;
import applications.IApplication;
import controller.ControllerService;
import models.entities.*;
import models.repositories.interfaces.*;
import models.repositories.RepositoryProvider;
import services.*;
import services.actionProviders.*;

import view.Action;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static void fillRepositoriesWithTestData(RepositoryProvider repProvider){

        ICommentRepository commentRepository =  repProvider.getCommentRepository();
        commentRepository.add(new Comment(1, 1, 1, RequestState.STARTED, Date.from(ZonedDateTime.now().toInstant()), "NOPE"));

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
        userRepository.add(new User(1, "test", "HnO6qrPRkmsVkONr/TOUvQ==", true, new byte[]{83, 59, 110, 125, 48, 77, 18, 99, -18, -69, -67, -103, -67, 44, 124, -61}, rolesIds));
        IResidentRepository residentRepository = repProvider.getResidentRepository();
        residentRepository.add(new Resident(1, 1, "Saaas", "Sooos", true, "19A", "Test"));
        IRequestRepository requestRepository = repProvider.getRequestRepository();
        requestRepository.add(new Request(1, RequestType.COLLECTIVE, RequestState.STOPPED, "Somethong's wrong", 1, 1,  Date.from(ZonedDateTime.now().toInstant()), false));

    }

    public static void testConnection(ConfigRepository configRep) {
        String url = configRep.getPropertyValue("db.url");
        String username = configRep.getPropertyValue("db.username");
        String password = configRep.getPropertyValue("db.password");
        RepositoryProvider.init(configRep);
//        IResidentRepository residentRep = RepositoryProvider.getInstance().getResidentRepository();
//        Resident res = new Resident(2, 2, "Saaas", "Sooos", true, "19A", "Test");
//        residentRep.add(res);
        IUserRepository userRepository = RepositoryProvider.getInstance().getUserRepository();
        User user = userRepository.getUserById(8);
        userRepository.authenticate("test", "test");

        System.out.print(user.getUserId());
//        IRoleRepository roleRepository = RepositoryProvider.getInstance().getRoleRepository();
//        LinkedList<Permission> permissions = new LinkedList<>();
//        Permission permission1 = new Permission();
//        permission1.setId(Permissions.USER);
//        permission1.setLevel(PermissionLevel.FULL_RIGHTS);
//        Permission permission2 = new Permission();
//        permission2.setId(Permissions.COMMENT);
//        permission2.setLevel(PermissionLevel.FULL_RIGHTS);
//        Permission permission3 = new Permission();
//        permission3.setId(Permissions.REQUEST);
//        permission3.setLevel(PermissionLevel.FULL_RIGHTS);
//        Permission permission4 = new Permission();
//        permission4.setId(Permissions.ROLE);
//        permission4.setLevel(PermissionLevel.FULL_RIGHTS);
//
//        permissions.add(permission1);
//        permissions.add(permission2);
//        permissions.add(permission3);
//        permissions.add(permission4);
//        Role role = new Role(2, "Admin", permissions);
//        roleRepository.addRole(role);
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(url,
//                    username, password);
//
//            PreparedStatement preparedStatement = null;
//            try {
//                User usr =new User();
//                usr.setLogin("test");
//                usr.setPassword("test");
//
//                preparedStatement = connection.prepareStatement("insert into \"user\" (login, password, pssword_salt, active) values (?, ?, ?, ?)");
//                preparedStatement.setString(1, "test");
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//                while (resultSet.next()) {
//                    System.out.println(resultSet.getString("login"));
//                }
//            } catch (SQLException e) {
//                    throw new RuntimeException(e);
//            } finally {
//                preparedStatement.close();
//            }
//        } catch (Exception e) {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException ex) {}
//            }
//        }
    }

    public static void main(String[] args){
        ConfigRepository configRep = ConfigRepository.getInstance("C:\\VSU courses\\MaintanenceCompany\\config.txt");
//        testConnection(configRep);
//        return;

        RepositoryProvider repProvider = RepositoryProvider.init(configRep);
        if (configRep.getPropertyValue("repository_type").equals("IN_MEMORY")) {
            fillRepositoriesWithTestData(repProvider);
        }

//        RequestService rc = new RequestService();
//        System.out.println(rc.getClass());
//        System.out.println(rc.getClass());

        IApplication application = new ConsoleApplication();
        ControllerService controllerService = new ControllerService();
        ActionProviderContainer.init(new CommentActionProvider(),
                                    new RequestActionProvider(),
                                    new LoginActionProvider(),
                                    new UserActionProvider(),
                                    new RoleActionProvider(),
                                    new PermissionActionProvider(),
                                    new PaginationActionProvider());
        Action landing = ActionProviderContainer.getLoginActionProvider().getActionShow("", "", null, null, false);
//        Action login = loginActionProvider.getActionAdd("", "", landing, null);
        application.start(landing, controllerService);

    }
}
