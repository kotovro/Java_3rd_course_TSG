package controller;

import models.repositories.RepositoryProvider;
import services.UserService;
import view.ViewModel;

public class UserController {

    RepositoryProvider rep = RepositoryProvider.getInstance();
    UserService userService = new UserService();

    public UserController() {
        userService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        return userService.fillView(id);
    }

    public ViewModel update(ViewModel viewModel) {
        return userService.update(viewModel);

    }
    public ViewModel add(String str) {
        return userService.fillView("-1");
    }
    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        return userService.getList();
    }
}
