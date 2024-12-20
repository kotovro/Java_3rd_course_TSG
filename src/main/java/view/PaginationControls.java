package view;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PaginationControls {
    FIRST("First page"),
    LAST("Last page"),
    NEXT("Next page"),
    PREVIOUS("Prev page"),
    CURRENT("Current page"),
    SIZE("Page size");


    private final String paginationControlName;

    PaginationControls(String controlName) {
        paginationControlName = controlName;
    }
    private static final Map<String, PaginationControls> lookup = new HashMap<>();

    static {
        for (PaginationControls control : PaginationControls.values()) {
            lookup.put(control.paginationControlName, control);
        }
    }

    public static PaginationControls name(String paginationControlName) {
        return lookup.get(paginationControlName);
    }
}
