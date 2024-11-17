package models.entities;

import lombok.Getter;

public enum PermissionLevel {
        NOT_ACCESSIBLE(0),
        READONLY(1),
        ADD_NEW(2),
        FULL_RIGHTS(3);

        @Getter
        private final int priority;

        PermissionLevel(int priority) {
                this.priority = priority;
        }
        }
