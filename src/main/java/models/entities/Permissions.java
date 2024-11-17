package models.entities;

import lombok.Getter;

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
}
