package applications;

import controller.ControllerService;
import view.*;


import java.util.List;
import java.util.Scanner;

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
        ViewModel viewModel = null;
        while (action.getActionType() != Action.ActionType.EXIT)
        {
            switch (action.getActionType())
            {
                case SHOW:
                    try {
                        viewModel = controllerService.doAction(action, null);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                    action = display(viewModel);
                    break;
                case UPDATE:
                    updateContext(viewModel);
                    viewModel = controllerService.doAction(action, viewModel);
//                    DEFINITELY RESOLVE SHOW COMMAND DEPENDENCY!
                    if (viewModel.getErrorMessage() != null) {
                        action = action.getOnError();
                    } else {
                        action = action.getOnSuccess();
                    }
                    break;
                case ADD:
//                    viewModel = controllerService.doAction(action, null);
//                    action = add(viewModel);
//                    viewModel = controllerService.doAction(action, viewModel);
                    break;
                case DELETE:
//                    action = deleteContext();
                    break;

            }

        }
    }

    private Action add(ViewModel viewModel) {
        Scanner scanner = new Scanner(System.in);
        for (ViewField field : viewModel.getParameters())
        {
            if (field.isChangeable())
            {
                System.out.print("Enter " + field.getAttributeName() + ": ");
                String newFiledView = scanner.nextLine();
                if (newFiledView.length() > 0) {
                    field.setAttributeValue(newFiledView);
                }
            }
        }
        return new Action();
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
                System.out.print("Enter new " + field.getAttributeName() + "(empty to leave untouched): ");
                String newFiledView = scanner.nextLine();
                if (newFiledView.length() > 0) {
                    field.setAttributeValue(newFiledView);
                }
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
            case BACK -> "Back to list";
        };
    }
    private static void showCommands(ViewModel viewModel) {
        int i = 1;
        for (Action action: viewModel.getActionsList())
        {
            if (action.isInteractive()) {
                String actionName = action.getActionName() != null  && action.getActionName().length() > 0
                        ? action.getActionName()
                        : getCommandNameByType(action.getActionType()) + " ";
                System.out.print(Integer.toString(i) + ". " + actionName + " ");
            }
            i++;
        }
        System.out.println();
    }

    private static void showMenu(ViewModel viewModel) {
        System.out.println("Mock -------------");
    }


    private static Action display(ViewModel viewModel) {
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
        return action;
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


