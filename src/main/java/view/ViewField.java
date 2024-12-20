package view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ViewField {
    @Setter
    private String attributeName;
    @Setter
    private String attributeValue;
    @Setter
    private boolean isChangeable;
    @Setter
    private boolean isDisplayable;
    @Setter
    private ViewField valueFrom = null;
    @Setter
    private boolean isList = false;
    @Setter
    private boolean isListMultiple = false;
    @Setter
    private boolean isPagination = false;
    @Setter
    private String dataSource;

    private List<ValidationTypes> validators = new LinkedList<>();



    public ViewField() {

    }
    public ViewField(String attributeName, String attributeValue, boolean isChangeable, boolean isDisplayable) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.isChangeable = isChangeable;
        this.isDisplayable = isDisplayable;
    }

    public ViewField(String attributeName, String attributeValue, boolean isChangeable, boolean isDisplayable, ViewField valueFrom, boolean isList, String dataSource) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.isChangeable = isChangeable;
        this.isDisplayable = isDisplayable;
        this.valueFrom = valueFrom;
        this.isList = isList;
        this.dataSource = dataSource;
    }

}
