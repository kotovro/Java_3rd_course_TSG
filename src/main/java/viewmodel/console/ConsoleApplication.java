package viewmodel.console;

import services.RepositoryProvider;
import viewmodel.*;


import java.util.List;
import java.util.Scanner;
import java.lang.reflect.*;
public class ConsoleApplication implements IApplication {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Override
    public void start(Action action, ControllerService controllerService) {
        while (action.getActionType() != Action.ActionType.EXIT)
        {
            ViewModel viewModel = null;
            try {
                viewModel = controllerService.doAction(action);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            display(viewModel);
        }
    }

    private static void clearScreen() {
        for (int i = 0; i < 100; i++)
        {
            System.out.println("");
        }
    }
    private static void showError(String message)
    {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
    private static void showContext(ViewModel modelView) {
        System.out.println(modelView.getTitle());
        for (ViewField field : modelView.getParameters())
        {
            if (field.isDisplayable()) {
                System.out.println(field.getAttributeName() + ": " + field.getAttributeValue());
            }
        }
    }

    private static void updateContext(ViewModel viewModel) {
        Scanner scanner = new Scanner(System.in);
        for (ViewField field : viewModel.getParameters())
        {
            if (field.isChangeable())
            {
                System.out.print("Enter new " + field.getAttributeName() + ": ");
                String newFiledView = scanner.nextLine();
                field.setAttributeValue(newFiledView);
            }
        }

    }
    private static String getCommandNameByType(Action.ActionType action) {
        return switch (action)
        {
            case ADD -> "Add new";
            case UPDATE -> "Update";
            case DELETE -> "Delete";
            case EXIT -> "Exit";
            case RETURN -> "Return";
            case SHOW -> "";
        };
    }
    private static void showCommands(ViewModel viewModel) {
        int i = 1;
        for (Action action: viewModel.getActionsList())
        {
            System.out.print(Integer.toString(i) + ". " + getCommandNameByType(action.getActionType()) + " ");
            i++;
        }
        System.out.println();
    }

    private static void showMenu(ViewModel viewModel) {
        System.out.println("Mock -------------");
    }


    private static void display(ViewModel viewModel) {
        Action action = null;
        while (action == null) {
            clearScreen();

            if (viewModel.getErrorMessage() != null) {
                showError(viewModel.getErrorMessage());
            }
            showContext(viewModel);
            showCommands(viewModel);
            showMenu(viewModel);

            action = getNextAction(viewModel);
        }
    }

    private static Action getNextAction(ViewModel viewModel) {
//        System.in.reset();

        Action result = null;
        List<Action> actions = viewModel.getActionsList();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter action number: ");
        int actionNumber = sc.nextInt();
        if (actionNumber < 1 || actionNumber > actions.size()) {
            viewModel.setErrorMessage("Invalid action");
        } else {
            viewModel.setErrorMessage(null);
            result = actions.get(actionNumber - 1);
        }
        return result;
    }

    private static void update(){

    }
}


