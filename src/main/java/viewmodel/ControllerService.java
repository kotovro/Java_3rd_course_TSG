package viewmodel;

import services.IController;
import services.RepositoryProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerService {
    public ViewModel doAction(Action action) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> c = null;
        try {
            c = Class.forName(action.getController());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        IController controller = (IController) c.getDeclaredConstructor().newInstance();
        controller.setRepositoryProvider(RepositoryProvider.getInstance());

        Method m = null;
        try {
            m = c.getMethod(action.getAction(), String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ViewModel viewModel = null;
        try {
            viewModel = (ViewModel) m.invoke(controller, action.getParameter());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return viewModel;
    }
}
