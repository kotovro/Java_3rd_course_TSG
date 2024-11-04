package controller;

import view.Action;
import view.ViewModel;

import java.lang.reflect.Method;

public class ControllerService {
    public ViewModel doAction(Action action, ViewModel viewModel) throws RuntimeException {
        Class<?> c = null;
        Method m = null;

        String route = action.getRoute();
        String controllerName = getControllerFromRoute(route);
        String actionName = getActionFromRoute(route);
        Class paramClass = viewModel == null ? String.class : ViewModel.class;
        Object parameter = viewModel == null ? action.getParameter() : viewModel;

        try {

            c = Class.forName(controllerName);

            Object controller =  c.getDeclaredConstructor().newInstance();

            m = c.getMethod(actionName, paramClass);

            viewModel = (ViewModel) m.invoke(controller, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return viewModel;
    }

    private String getControllerFromRoute(String route) {
        return  "controller." + route.split("/")[0] + "Controller";
    }

    private String getActionFromRoute(String route) {
        return  route.split("/")[1];
    }

}
