package controller;

import models.repositories.RepositoryProvider;
import services.RequestService;
import view.ViewModel;

public class RequestController extends AbstractController {

    RequestService requestService = new RequestService();


    public RequestController(String userToken) {
        super(userToken);
        requestService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        ViewModel vm = requestService.fillView(id);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel update(ViewModel viewModel) {
        int userId = permissionService.getUserIdFromToken(userToken);
        ViewModel vm = requestService.update(viewModel, userId);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel add(String str) {
        ViewModel vm = requestService.fillView("-1");
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        ViewModel vm = requestService.getList();
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

}
