package controller;

import models.repositories.RepositoryProvider;
import services.PermissionsService;
import services.ValidatorService;

public abstract class AbstractController {
    protected String userToken;
    protected PermissionsService permissionService = new PermissionsService();
    protected RepositoryProvider rep = RepositoryProvider.getInstance();
    protected ValidatorService validator = new ValidatorService();

    public AbstractController(String userToken) {
        this.userToken = userToken;
        permissionService.setRepositoryProvider(rep);
    }
}
