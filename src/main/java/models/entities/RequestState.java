package models.entities;

public enum RequestState {
    STARTED("Started"),
    STOPPED("Stopped"),
    FINISHED("Finished");

    private final String state;
    RequestState(String state) {
        this.state = state;
    }
//    STARTED,
//    STOPPED,
//    FINISHED;
}
