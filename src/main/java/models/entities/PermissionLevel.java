package models.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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

        private static final Map<Integer, PermissionLevel> lookup = new HashMap<>();

        static {
                for (PermissionLevel level : PermissionLevel.values()) {
                        lookup.put(level.getPriority(), level);
                }
        }

        public static PermissionLevel name(Integer level) {
                return lookup.get(level);
        }
        }
