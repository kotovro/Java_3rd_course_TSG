package controller;

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
        String validationResult = validator.validate(viewModel);
        if (!validationResult.isEmpty()) {
            viewModel.setErrorMessage(validationResult);
            return viewModel;
        }

        ViewModel vm =  roleService.update(viewModel);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);

    }
    public ViewModel add(String str) {
        ViewModel vm = roleService.fillView("-1");
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel delete(String str) {
        ViewModel vm = roleService.deleteRole(str);
        vm.setUserToken(userToken);
        return permissionService.applyPermissions(vm);
    }

    public ViewModel getList(String param) {
        ViewModel vm = roleService.getList(param);
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
