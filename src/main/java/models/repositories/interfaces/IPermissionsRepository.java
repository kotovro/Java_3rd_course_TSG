package models.repositories.interfaces;

import models.entities.Permission;

import java.util.List;

public interface IPermissionsRepository {
    List<Permission> getPermissionsList(List<Integer> permissionIds);
    Permission getPermissionByName(String permissionName);
}
