package controller;


import models.repositories.RepositoryProvider;
import services.LoginService;
import view.ViewModel;

public class LoginController {
    RepositoryProvider rep = RepositoryProvider.getInstance();
    LoginService service = new LoginService();


    public LoginController() {
        service.setRepositoryProvider(rep);
    }

    public ViewModel login(String str) {
        return service.fillView();
    }

    public ViewModel authenticate(ViewModel vm) {
        return service.authenticate(vm);
    }

    public ViewModel landing(String userId) {
        return service.getMenu();
    }
}
