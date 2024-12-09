package services;

import services.actionProviders.ActionProviderContainer;
import services.actionProviders.IActionProvider;

public class ListRouteProvider {

    public static String getRoute(RouteType routeType) {
        switch (routeType) {
            case ROLE -> {
                return "list/roles";
            }
            case RESIDENT -> {
                return "list/resident";
            }
            case TYPE -> {
                return "list/type";
            }
            case STATUS -> {
                return "list/status";
            }
        }
        return "";
    }
}
