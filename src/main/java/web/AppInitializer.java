package web;

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

        RepositoryProvider.init(configRep);
        ActionProviderContainer.init(new CommentActionProvider(),
                new RequestActionProvider(),
                new LoginActionProvider(),
                new UserActionProvider(),
                new RoleActionProvider(),
                new PermissionActionProvider(),
                new PaginationActionProvider());
        System.out.println("Application is starting...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application is stopping...");
    }
}