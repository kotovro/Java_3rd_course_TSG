package web;

import applications.ConsoleApplication;
import applications.IApplication;
import controller.ControllerService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import models.repositories.RepositoryProvider;
import services.ConfigRepository;
import services.actionProviders.*;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConfigRepository configRep = ConfigRepository.getInstance("C:\\VSU courses\\MaintanenceCompany\\config.txt");
//        testConnection(configRep);
//        return;

        RepositoryProvider.init(configRep);
        IActionProvider commentActionService = new CommentActionProvider();
        IActionProvider requestActionService = new RequestActionProvider();
        IActionProvider loginActionProvider = new LoginActionProvider();
        IActionProvider userActionProvider = new UserActionProvider();
        IActionProvider roleActionProvider = new RoleActionProvider();
        IActionProvider permissionActionProvider = new PermissionActionProvider();
        ActionProviderContainer.init(commentActionService,
                requestActionService,
                loginActionProvider,
                userActionProvider,
                roleActionProvider,
                permissionActionProvider);
        System.out.println("Application is starting...");
        // Perform initialization logic here, e.g., setting up resources, configs, etc.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application is stopping...");
        // Perform cleanup logic here.
    }
}