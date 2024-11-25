package models.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    COMMENT(0),
    REQUEST(1),
    USER(2),
    ROLE(3);

    @Getter
    private final int id;

    Permissions(int id) {
        this.id = id;
    }

    private static final Map<Integer, Permissions> lookup = new HashMap<>();

    static {
        for (Permissions permission : Permissions.values()) {
            lookup.put(permission.getId(), permission);
        }
    }

    public static Permissions name(Integer permissionId) {
        return lookup.get(permissionId);
    }
}
