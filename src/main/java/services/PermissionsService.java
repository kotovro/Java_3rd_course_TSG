package services;

import lombok.Setter;
import models.entities.*;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.IRoleRepository;
import models.repositories.interfaces.IUserRepository;
import view.Action;
import view.ViewModel;

import java.util.*;
import java.util.stream.Collectors;


public class PermissionsService {

    @Setter
    private RepositoryProvider repositoryProvider;

    public ViewModel applyPermissions(ViewModel vm) {
        IUserRepository userRep = repositoryProvider.getUserRepository();
        User user = userRep.getUserByToken(vm.getUserToken());

        if (user.getRoles().isEmpty()) {
            return vm;
        }
        IRoleRepository roleRep = repositoryProvider.getRoleRepository();
        List<Permission> currentUserPermissions = new LinkedList<>();
        List<Action> availableActions = new LinkedList<>();
        for (int role : user.getRoles()) {
            Role rl = roleRep
                    .getRoleById(role);
            if (!rl.getPermissions().isEmpty()) {
                currentUserPermissions.addAll(rl.getPermissions());
            }
        }

        List<Permission> maxUserPermissions = new LinkedList<>();
        for (Permissions permissions: Permissions.values()) {
            List<Permission> temp = currentUserPermissions.stream().collect(Collectors.groupingBy(Permission::getId)).get(permissions);
            Permission maxPerm = new Permission();
            maxPerm.setLevel(PermissionLevel.NOT_ACCESSIBLE);
            maxPerm.setId(temp.get(0).getId());
            for (Permission permission: temp) {
                if (permission.getLevel().getPriority() > maxPerm.getLevel().getPriority()) {
                    maxPerm.setLevel(permission.getLevel());
                }
            }
            maxUserPermissions.add(maxPerm);
        }
        List<Action> actions = vm.getActionsList();
        for (Action action: actions) {
            PermissionLevel usrPrm = null;
            PermissionLevel actionPrm = action.getPermissionLevel();
            boolean isActionAccessible = true;
            if (actionPrm != null) {
                try {
                    usrPrm = maxUserPermissions.stream().filter(p -> p.getId().equals(action.getPermissionId())).findFirst().get().getLevel();
                    if (usrPrm.getPriority() < actionPrm.getPriority()) {
                        isActionAccessible = false;
                    }
                } catch (Exception e) {
                    isActionAccessible = false;
                }
            }
            if (isActionAccessible) {
                availableActions.add(action);
            }
        }
        actions.clear();
        actions.addAll(availableActions);
        return vm;
    }
}
