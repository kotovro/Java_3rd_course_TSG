package applications;

import services.ControllerService;
import view.Action;

public interface IApplication {
    void start(Action action, ControllerService controllerService);
}
