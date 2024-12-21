package controller;

import models.repositories.RepositoryProvider;
import services.PermissionsService;
import services.UserService;
import view.ViewModel;

import javax.swing.text.View;

public class UserController extends AbstractController{
    UserService userService = new UserService();

    public UserController(String userToken) {
        super(userToken);
        userService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        ViewModel vm = userService.fillView(id);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel update(ViewModel viewModel) {
        String validationResult = validator.validate(viewModel);
        if (!validationResult.isEmpty()) {
            viewModel.setErrorMessage(validationResult);
            return viewModel;
        }

        ViewModel vm = userService.update(viewModel);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel add(String str) {
        ViewModel vm = userService.fillView("-1");
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        ViewModel vm = userService.getList(param);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }
}
