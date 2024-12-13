package services;

import lombok.Setter;
import models.entities.*;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.*;
import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;
import view.Action;
import view.ViewField;
import view.ViewModel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RoleService {

    @Setter
    private RepositoryProvider repositoryProvider;

    public ViewModel fillView(String roleIdStr) {
        int roleId = Integer.parseInt(roleIdStr);
        ViewModel vm = new ViewModel();

        Role role;
        IRoleRepository roleRep = repositoryProvider.getRoleRepository();
        if (roleId > 0) {
            role = roleRep.getRoleById(roleId);
            vm.setTitle("Role " + role.getName());
        }
        else {
            vm.setTitle("New role");
            role = new Role();
        }
        if (role == null) {
            return vm;
        }

        List<ViewField> parameters = vm.getParameters();
        parameters.add(new ViewField("Role Id", roleIdStr, false, false));
        parameters.add(new ViewField("Permissions\n", "", false, false));
        parameters.add(new ViewField("Role name", role.getName(), true, true));

        List<Permission> rolePermissions = role.getPermissions();
        IActionProvider permissionProvider = ActionProviderContainer.getPermissionActionProvider();
        for (Permissions perm : Permissions.values()) {
            Permission rolePermission = new Permission(perm, PermissionLevel.NOT_ACCESSIBLE);
            rolePermissions.stream().filter(p -> p.getId().equals(perm)).findFirst().ifPresent(p -> {rolePermission.setLevel(p.getLevel());});
            String param = roleIdStr + "/" + rolePermission.getId().getId();
            Action updatePermission = permissionProvider.getActionUpdate(param, "", null, null);
            Action addPermission = permissionProvider.getActionAdd(param,
                    rolePermission.getId().name() + " -> " + rolePermission.getLevel().name(),
                    updatePermission, null);
            addPermission.setListItem(true);
            addPermission.setInteractive(true);
            addPermission.setDataSource(ListRouteProvider.getRoute(RouteType.PERMISSION));
            vm.addCommand(addPermission);
        }
        IActionProvider roleActionProvider = ActionProviderContainer.getRoleActionProvider();
        Action show = roleActionProvider.getActionShow(roleIdStr, "", null, null, false);
        Action back = roleActionProvider.getActionBack("", "Back to roles list");
        Action update = roleActionProvider.getActionUpdate(roleIdStr, "Update role", show, show);
        vm.addCommand(update);

        if (roleId > 0) {

            Action add = roleActionProvider.getActionAdd("", "Add", update, update);
            vm.addCommand(add);

            Action delete = roleActionProvider.getActionDelete(roleIdStr, "Delete", back, back);
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
        viewModel.setTitle("Roles list");
        IRoleRepository rep = repositoryProvider.getRoleRepository();
        List<Role> reqList = rep.getRoleList();
        IActionProvider roleActionProvider = ActionProviderContainer.getRoleActionProvider();
        for (Role role : reqList)
        {
            Action action = roleActionProvider.getActionShow( Integer.toString(role.getId()), "Role " + role.getName() + "\n", null, null, true);
            action.setListItem(true);
            viewModel.getActionsList().add(action);
        }
        Action updateNew = roleActionProvider.getActionUpdate("-1", "", null, null);
        Action add = roleActionProvider.getActionAdd("-1", "Add new role", updateNew, null);
        viewModel.getActionsList().add(add);
        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.getActionsList().add(exit);
        return viewModel;
    }

    public ViewModel update(ViewModel viewModel) {
        int id = Integer.parseInt(viewModel.getFieldValueByAttributeName("Role Id"));
        IRoleRepository rep = repositoryProvider.getRoleRepository();
        Role role = null;
        if (id < 0)
        {
            role = new Role();
        } else {
            role = rep.getRoleById(id);
        }
        role.setName(viewModel.getFieldValueByAttributeName("Role name"));
        id = rep.updateRole(role);

        return fillView(Integer.toString(id));
    }

    public ViewModel add() {
        return fillView("-1");
    }

    public ViewModel editPermission(String param) {
        Permission permission = getPermissionFromRole(param);
        ViewModel vm = new ViewModel();
        List<ViewField> vmParams = vm.getParameters();
        vmParams.add(new ViewField(permission.getId().name() + "permission level", permission.getLevel().name(), true, false));
        vmParams.add(new ViewField("role/permission", param, false, false));
        return vm;
    }

    public ViewModel updatePermission(ViewModel vm) {
        String params = vm.getFieldValueByAttributeName("role/permission");
        String roleIdStr = params.split("/")[0];
        Permission permission = getPermissionFromRole(params);
        String permissionLevel = vm.getFieldValueByAttributeName(permission.getId().name() + "permission level");
        PermissionLevel lvl = PermissionLevel.valueOf(permissionLevel);
        permission.setLevel(lvl);

        IRoleRepository rep = repositoryProvider.getRoleRepository();
        rep.updateRolePermission(Integer.parseInt(roleIdStr), permission);
        return fillView(roleIdStr);
    }

    private Permission getPermissionFromRole(String param) {
        String[] params = param.split("/");
        int roleId = Integer.parseInt(params[0]);
        int permissionId = Integer.parseInt(params[1]);

        IRoleRepository rep = repositoryProvider.getRoleRepository();
        List<Permission> savedPermissions = rep.getRoleById(roleId).getPermissions();

        Optional<Permission> savedPermission = savedPermissions.stream().filter(r -> r.getId().getId() == permissionId).findFirst();

        return savedPermission.orElse(new Permission(Permissions.name(permissionId), PermissionLevel.NOT_ACCESSIBLE));
    }

    public ViewModel getPermissionsList(String roleId) {
        ViewModel viewModel = new ViewModel();
        viewModel.setTitle("Permissions list");
        IRoleRepository rep = repositoryProvider.getRoleRepository();
        Role role = rep.getRoleById(Integer.parseInt(roleId));
        List<Permission> permissions = role.getPermissions();
        IActionProvider permissionActionProvider = ActionProviderContainer.getPermissionActionProvider();

        for (Permissions prm : Permissions.values())
        {
            if (permissions.stream().noneMatch(p -> p.getId().equals(prm)))
            {
                String param = roleId + "/" + prm.getId();
                Action updatePermission = permissionActionProvider.getActionUpdate(param, "", null, null);
                viewModel.addCommand(permissionActionProvider.getActionAdd(param,
                        prm.name(),
                        updatePermission, null));
            }
        }
        IActionProvider roleActionProvider = ActionProviderContainer.getRoleActionProvider();
        Action back = roleActionProvider.getActionShow(roleId, "Back to role", null, null, true);
        viewModel.addCommand(back);
        Action exit = new Action();
        exit.setActionType(Action.ActionType.EXIT);
        viewModel.addCommand(exit);
        return viewModel;
    }
}
