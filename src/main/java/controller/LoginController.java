package controller;


import models.repositories.RepositoryProvider;
import services.LoginService;
import view.ViewModel;

public class LoginController extends AbstractController {
    LoginService service = new LoginService();


    public LoginController(String userToken) {
        super(userToken);
        service.setRepositoryProvider(rep);
    }

    public ViewModel login(String str) {
        return service.fillView();
    }

    public ViewModel authenticate(ViewModel vm) {
        return service.authenticate(vm);
    }

    public ViewModel landing(String userId) {
        ViewModel vm = service.getMenu();
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }
}
