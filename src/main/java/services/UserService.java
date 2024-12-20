package services;

import models.entities.*;
import models.repositories.RepositoryProvider;
import lombok.Setter;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import models.repositories.interfaces.IUserRepository;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import services.actionProviders.PaginationActionProvider;
import services.actionProviders.RoleActionProvider;
import view.Action;
import view.PaginationControls;
import view.ViewField;
import view.ViewModel;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.text.View;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class UserService {

    @Setter
    private RepositoryProvider repositoryProvider;


    public ViewModel fillView(String param) {

        String userIdStr = "-1";
        String[] params = param.split(";");
        for (String kvPair: params) {
            String[] keyValue = kvPair.split(":");
            if (keyValue[0].equals("id")) {
                userIdStr = keyValue[1];
            }
        }

        String pagingParams = param.replace("id:" + userIdStr, "");
        int userId = Integer.parseInt(userIdStr);
        ViewModel vm = new ViewModel();


        IUserRepository userRep = repositoryProvider.getUserRepository();
        User user = null;
        if (userId > 0) {
            vm.setTitle("User " + Integer.toString(userId));
            user = userRep.getUserById(userId);
        }
        else {
            vm.setTitle("New user");
            user = new User();
        }
        if (user == null) {
            return vm;
        }

        List<ViewField> parameters = vm.getParameters();
        parameters.add(new ViewField("User Id", userIdStr, false, true));
        parameters.add(new ViewField("Login", user.getLogin(),userId < 0, true));
        parameters.add(new ViewField("Password", "", userId < 0, userId < 0));

        IRequestRepository requestRep = repositoryProvider.getRequestRepository();
        parameters.add(new ViewField(
                "Requests by this user",
                            Integer.toString(requestRep.getRequestByUser(userId)),
                false,
                true));

        List<Role> roles = userRep.getRoleList(userId);
        if (roles.isEmpty()) {
            parameters.add(new ViewField("NO ROLES", Integer.toString(-1),
                    true, true, null, false, true, false,
                    ListRouteProvider.getRoute(RouteType.ROLE)));
            }
        for (Role role : roles) {
            parameters.add(new ViewField(role.getName(), Integer.toString(role.getId()),
                    true, true, null, false, true, false,
                    ListRouteProvider.getRoute(RouteType.ROLE)));
        }
        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();
        Action show = userActionProvider.getActionShow(userIdStr, "", null, null, false);
        Action back = userActionProvider.getActionBack(pagingParams, "Back to users list");
        Action update = userActionProvider.getActionUpdate(userIdStr, userId > 0 ? "Update user" : "Add user", show, show);
        vm.addCommand(update);

        if (userId > 0) {
            Action add = userActionProvider.getActionAdd("-1", "Add", update, update);
            vm.addCommand(add);

            Action delete = userActionProvider.getActionDelete(userIdStr, "Delete", back, back);
            vm.addCommand(delete);
        }

        vm.addCommand(back);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        vm.addCommand(exit);

        return vm;
    }

    public ViewModel getList(String pageParams) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Users list");
        String[] params = pageParams.split(";");
        int pageNumber = 1;
        int pageSize = 10;
        for (String kvPair: params) {
            String[] keyValue = kvPair.split(":");
            if (keyValue[0].equals("pn")) {
                pageNumber = Integer.parseInt(keyValue[1]);
            } else if (keyValue[0].equals("ps")) {
                pageSize = Integer.parseInt(keyValue[1]);
            }
        }
        IUserRepository rep = repositoryProvider.getUserRepository();
        List<User> usrList = rep.getUserList(pageNumber, pageSize);
        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();
        for (User user : usrList)
        {
            Action action = userActionProvider.getActionShow( Integer.toString(user.getUserId()), "User " + user.getLogin() + "\n", null, null, true);
            action.setListItem(true);
            viewModel.getActionsList().add(action);
        }
        Action updateNew = userActionProvider.getActionUpdate("-1", "", null, null);
        Action add = userActionProvider.getActionAdd("id:-1;" + pageParams, "Add new user", updateNew, null);
        viewModel.getActionsList().add(add);

        Action actionList = userActionProvider.getActionList("", "", null, null);
        PaginationActionProvider paginationActionProvider = ActionProviderContainer.getPaginationActionProvider();
        int userCount = rep.getUserCount();
        viewModel.getActionsList().addAll(paginationActionProvider.getPaginationActions(actionList, userCount, pageNumber, pageSize));

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    public ViewModel update(ViewModel viewModel) {
        int id = Integer.parseInt(
                viewModel.getParameters()
                        .stream()
                        .filter(p -> p.getAttributeName().equals("User Id"))
                        .findFirst()
                        .get()
                        .getAttributeValue());
        IUserRepository rep = repositoryProvider.getUserRepository();
        User user;
        if (id < 0)
        {
            user = new User();
        } else {
            user = rep.getUserById(id);
        }
        user.setLogin(viewModel.getFieldValueByAttributeName("Login"));

        String newPassword = viewModel.getFieldValueByAttributeName("Password");
        if (!newPassword.equals(user.getPassword())) {
            user.setPassword(newPassword);
            rep.hashUserPassword(user);
        }

        List<Integer> roles = viewModel.getParameters().stream()
                .filter(ViewField::isListMultiple)
                .map(f -> Integer.parseInt(f.getAttributeValue()))
                .toList();
        user.setRoles(roles);
        id = rep.updateUser(user);

        return fillView(Integer.toString(id));
    }

    public ViewModel add() {
        return fillView("-1");
    }

}
