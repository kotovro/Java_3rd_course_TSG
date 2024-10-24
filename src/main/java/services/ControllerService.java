package services;

import controllers.IController;
import view.Action;
import view.ViewModel;

import java.lang.reflect.Method;

public class ControllerService {
    public ViewModel doAction(Action action, ViewModel viewModel) throws RuntimeException {
        Class<?> c = null;
        Method m = null;

        Class paramClass = viewModel == null ? String.class : ViewModel.class;
        Object parameter = viewModel == null ? action.getParameter() : viewModel;

        try {
            c = Class.forName(action.getController());

            IController controller = (IController) c.getDeclaredConstructor().newInstance();
            controller.setRepositoryProvider(RepositoryProvider.getInstance());

            m = c.getMethod(action.getAction(), paramClass);

            viewModel = (ViewModel) m.invoke(controller, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return viewModel;
    }
}
