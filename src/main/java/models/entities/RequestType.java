package models.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum RequestType {
    COLLECTIVE(1),
    SINGLE_PERSON(2);

    @Getter
    private int requestTypeId;

    RequestType(int id) {
        requestTypeId = id;
    }
    private static final Map<Integer, RequestType> lookup = new HashMap<>();

    static {
        for (RequestType type : RequestType.values()) {
            lookup.put(type.requestTypeId, type);
        }
    }

    public static RequestType name(Integer type) {
        return lookup.get(type);
    }
}
