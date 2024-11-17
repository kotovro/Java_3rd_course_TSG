package controller;

import models.repositories.RepositoryProvider;
import services.PermissionsService;

public abstract class AbstractController {
    protected String userToken;
    protected PermissionsService permissionService = new PermissionsService();
    protected RepositoryProvider rep = RepositoryProvider.getInstance();

    public AbstractController(String userToken) {
        this.userToken = userToken;
        permissionService.setRepositoryProvider(rep);
    }
}
