package models.repositories.implementations.inmemory;

import models.entities.Permission;
import models.entities.Request;
import models.entities.Role;
import models.repositories.interfaces.IRoleRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class RoleRepositoryInMemory implements IRoleRepository {
    private List<Role> roles = new LinkedList<>();


    @Override
    public Role getRoleById(int id) {
        return roles.stream().filter(r -> r.getId() == id).findFirst().get();
    }

    @Override
    public void addRole(Role role) {
        if (!roles.isEmpty()) {
            int maxIndex = roles.stream().max(Comparator.comparing(r -> r.getId())).get().getId();
            role.setId(maxIndex + 1);
        }
        roles.add(role);
    }

    @Override
    public int updateRole(Role role) {
        if (role.getId() == -1)
        {
            addRole(role);
        } else {
            Role temp = getRoleById(role.getId());
            temp.updateFromObject(role);
        }
        return role.getId();
    }

    @Override
    public void updateRolePermission(int roleId, Permission permission) {
        Role role = getRoleById(roleId);
        role.getPermissions().stream()
                .filter(p -> p.getId() == permission.getId())
                .findFirst()
                .get()
                .setLevel(permission.getLevel());
    }

    @Override
    public List<Role> getRoleList() {
        return roles;
    }
}
