package services;

import lombok.Setter;
import models.entities.User;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.IUserRepository;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.List;

public class LoginService {

    @Setter
    private RepositoryProvider repositoryProvider;

    public ViewModel getMenu(){
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Main menu");

        IActionProvider requestActionProvider = ActionProviderContainer.getRequestActionProvider();
        Action showRequest = requestActionProvider.getActionList("ps:10;pn:1", "Show requests\n", null, null);
        // add user to all actions
        viewModel.addCommand(showRequest);

        IActionProvider userActionProvider = ActionProviderContainer.getUserActionProvider();
        Action showUsers = userActionProvider.getActionList("ps:10;pn:1", "Show users\n", null, null);
        viewModel.addCommand(showUsers);

        IActionProvider role = ActionProviderContainer.getRoleActionProvider();
        Action showRoles = role.getActionList("ps:10;pn:1", "Show roles\n", null, null);
        viewModel.addCommand(showRoles);
        //        IActionProvider userAction = ActionProviderContainer.getRequestActionProvider();
//        Action updateNew = requestActionProvider.getActionUpdate("-1", "", null, null);
//        Action add = requestActionProvider.getActionAdd("-1", "Add new request", updateNew, null);
//        viewModel.getActionsList().add(add);
        Action exit = new Action();
        exit.setActionName("Exit");
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    public ViewModel fillView() {
        ViewModel vm = new ViewModel();
        vm.setTitle("Please login:");
        List<ViewField> parameters = vm.getParameters();
        parameters.add(new ViewField("Login", "", true, true));
        parameters.add(new ViewField("Password", "", true, true));

        Action onSuccess = ActionProviderContainer.getLoginActionProvider().getActionList("", "", null, null);
        Action update = ActionProviderContainer.getLoginActionProvider().getActionUpdate("", "Enter login and password", onSuccess, null);
        vm.addCommand(update);

        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        vm.addCommand(exit);
        return vm;
    }

    public ViewModel authenticate(ViewModel viewModel) {
        String login = viewModel.getFieldValueByAttributeName("Login");
        String password = viewModel.getFieldValueByAttributeName("Password");
        IUserRepository rep = repositoryProvider.getUserRepository();
        User usr = rep.authenticate(login, password);
        if (usr.getLogin() == null) {
            viewModel.setErrorMessage("Invalid login or password");
            List<ViewField> list = viewModel.getParameters();
            for (ViewField viewField : list) {
                viewField.setAttributeValue(null);
            }
            return viewModel;
        }
        viewModel.setErrorMessage(null);
        viewModel.setUserToken(rep.getUserToken(usr.getUserId()));
        return viewModel;
    }
}
