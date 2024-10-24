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

}
