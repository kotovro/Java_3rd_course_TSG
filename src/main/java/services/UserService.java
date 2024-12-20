package services;

import models.entities.Request;
import models.entities.RequestState;
import models.entities.RequestType;
import models.repositories.RepositoryProvider;
import lombok.Setter;
import models.entities.User;
import models.repositories.interfaces.IRequestRepository;
import models.repositories.interfaces.IResidentRepository;
import models.repositories.interfaces.IStaffMemberRepository;
import models.repositories.interfaces.IUserRepository;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import services.actionProviders.RoleActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.text.View;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

public class UserService {

    @Setter
    private RepositoryProvider repositoryProvider;


    public ViewModel fillView(String userIdStr) {
        int userId = Integer.parseInt(userIdStr);
        ViewModel vm = new ViewModel();


        User user = null;
        if (userId > 0) {
            vm.setTitle("User " + Integer.toString(userId));
            IUserRepository userRep = repositoryProvider.getUserRepository();
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
        parameters.add(new ViewField("User Id", userIdStr, false, false));
        parameters.add(new ViewField("Login", user.getLogin(), true, false));
        parameters.add(new ViewField("Password", user.getPassword(), true, false));

        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();
        Action show = userActionProvider.getActionShow(userIdStr, "", null, null, false);
        Action back = userActionProvider.getActionBack("", "Back to users list");
        Action update = userActionProvider.getActionUpdate(userIdStr, "Update user", show, show);
//        Action addRole = ActionProviderContainer.getRoleActionProvider().getActionAdd("-1", "Add role", update, null);
        vm.addCommand(update);
//        vm.addCommand(addRole);

        if (userId > 0) {
            Action add = userActionProvider.getActionAdd("", "Add", update, update);
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

    public ViewModel getList() {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Users list");
        IUserRepository rep = repositoryProvider.getUserRepository();
        List<User> usrList = rep.getUserList();
        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();
        for (User user : usrList)
        {
            Action action = userActionProvider.getActionShow( Integer.toString(user.getUserId()), "User " + user.getLogin() + "\n", null, null, true);
            viewModel.getActionsList().add(action);
        }
        Action updateNew = userActionProvider.getActionUpdate("-1", "", null, null);
        Action add = userActionProvider.getActionAdd("-1", "Add new user", updateNew, null);
        viewModel.getActionsList().add(add);
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
        id = rep.updateUser(user);

        return fillView(Integer.toString(id));
    }

    public ViewModel add() {
        return fillView("-1");
    }

}
