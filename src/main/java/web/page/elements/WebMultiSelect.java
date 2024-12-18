package web.page.elements;

import models.entities.ListItem;

import java.util.List;

public record WebMultiSelect(String id, String label, String optionsURL, List<ListItem> options) {
}
