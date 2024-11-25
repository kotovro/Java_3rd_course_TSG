package models.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public enum RequestState {
    STARTED(1),
    STOPPED(2),
    FINISHED(3);

    @Getter
    private final int state;
    RequestState(int stateId) {
        this.state = stateId;
    }
    private static final Map<Integer, RequestState> lookup = new HashMap<>();

    static {
        for (RequestState name : RequestState.values()) {
            lookup.put(name.getState(), name);
        }
    }

    public static RequestState name(Integer weight) {
        return lookup.get(weight);
    }
}
