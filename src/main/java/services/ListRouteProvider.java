package services;
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
            case PERMISSION ->{
                return "list/permission";
            }
        }
        return "";
    }
}
