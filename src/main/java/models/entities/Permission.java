package models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Getter
@Setter
public class Permission {
    private Permissions id;
    private PermissionLevel level = PermissionLevel.NOT_ACCESSIBLE;

    public Permission(){}
}
