package view;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ValidationTypes {
    STRING_NOT_EMPTY("Value should not be empty"),
    INT_NOT_NEGATIVE("Value should not be negative"),
    STRING_VALID_INTEGER("Value is not valid integer"),
    USER_EXISTS("User not exist"),
    LOGIN_IS_UNIQUE("This login name is already occupied"),
    ROLE_IS_UNIQUE("Role with such name already exists");

    @Getter
    private final String errorMessage;

    ValidationTypes(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    private static final Map<String, ValidationTypes> lookup = new HashMap<>();

    static {
        for (ValidationTypes vt : ValidationTypes.values()) {
            lookup.put(vt.errorMessage, vt);
        }
    }

    public static ValidationTypes name(String errorMessage) {
        return lookup.get(errorMessage);
    }
}
