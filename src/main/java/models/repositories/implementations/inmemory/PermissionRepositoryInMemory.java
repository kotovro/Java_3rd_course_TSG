package models.repositories.implementations.inmemory;

import models.entities.Permission;
import models.entities.Permissions;
import models.repositories.interfaces.IPermissionsRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionRepositoryInMemory implements IPermissionsRepository {
    List<Permission> permissions = new LinkedList<>();

    @Override
    public List<Permission> getPermissionsList(List<Integer> permissionIds) {
        return permissions.stream().filter(p -> permissionIds.contains(p.getId())).collect(Collectors.toList());
    }

    @Override
    public Permission getPermissionByName(String permissionName) {
        return permissions.stream().filter(p -> p.getId().equals(permissionName)).findFirst().get();
    }
}
