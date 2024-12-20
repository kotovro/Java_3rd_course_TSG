package web.page.elements;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class PageContent {
    private List<WebButton> buttons = new LinkedList<>();
    private List<WebInput> inputs = new LinkedList<>();
    private List<WebSelect> selects = new LinkedList<>();
    private List<WebListItem> listItems = new LinkedList<>();
    private List<WebMultiSelect> multipleSelects = new LinkedList<>();
    @Setter
    private PaginationInfo paginationInfo = null;
    @Setter
    private String errorMessage = "";
}