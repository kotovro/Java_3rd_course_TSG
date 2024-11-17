package controller;

import models.repositories.RepositoryProvider;
import services.*;
import view.ViewModel;

public class CommentController extends AbstractController {

    CommentService service = new CommentService();

    public CommentController(String userToken) {
        super(userToken);
        service.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        ViewModel vm = service.fillView(id);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel update(ViewModel viewModel) {
        ViewModel vm = service.update(viewModel);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }
    public ViewModel add(String requestId) {
        ViewModel vm = service.fillEmptyView(requestId);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }
    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String requestId) {
        return service.getList(requestId);
    }
}
