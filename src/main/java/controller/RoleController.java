package controller;

import models.repositories.RepositoryProvider;
import services.RoleService;
import view.ViewModel;

public class RoleController extends AbstractController {
    RoleService roleService = new RoleService();

    public RoleController(String userToken) {
        super(userToken);
        roleService.setRepositoryProvider(rep);
    }

    public ViewModel show(String id) {
        ViewModel vm = roleService.fillView(id);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel update(ViewModel viewModel) {
        ViewModel vm =  roleService.update(viewModel);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);

    }
    public ViewModel add(String str) {
        ViewModel vm = roleService.fillView("-1");
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }
    public ViewModel delete(ViewModel viewModel) {
//        mock
        return viewModel;
    }

    public ViewModel getList(String param) {
        ViewModel vm = roleService.getList();
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel editPermission(String param) {
        return roleService.editPermission(param);
    }
    public ViewModel updatePermission(ViewModel viewModel) {
        return roleService.updatePermission(viewModel);
    }

    public ViewModel getPermissionsList(String roleId) {
        return roleService.getPermissionsList(roleId);
    }
}
