package models.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum RequestState {
    STARTED(1),
    STOPPED(2),
    FINISHED(3);

    @Getter
    private final int stateId;
    RequestState(int stateId) {
        this.stateId = stateId;
    }
    private static final Map<Integer, RequestState> lookup = new HashMap<>();

    static {
        for (RequestState name : RequestState.values()) {
            lookup.put(name.getStateId(), name);
        }
    }

    public static RequestState name(Integer weight) {
        return lookup.get(weight);
    }
}
