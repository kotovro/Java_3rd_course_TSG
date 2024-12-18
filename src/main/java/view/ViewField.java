package view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ViewField {
    private String attributeName;
    private String attributeValue;

    private boolean isChangeable;

    private boolean isDisplayable;
    private ViewField valueFrom = null;
    private boolean isList = false;
    private boolean isListMultiple = false;
    private boolean isPagination = false;
    private String dataSource;


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
