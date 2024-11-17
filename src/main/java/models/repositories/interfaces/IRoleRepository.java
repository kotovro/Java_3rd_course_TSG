package models.repositories.interfaces;

import models.entities.Role;
import models.entities.Permission;

import java.util.List;

public interface IRoleRepository {
    Role getRoleById(int id);
    void addRole(Role role);
    List<Role> getRoleList();
    int updateRole(Role role);
    void updateRolePermission(int roleId, Permission permission);
}
