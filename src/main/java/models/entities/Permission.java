package models.entities;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Permission {
    private Permissions id;
    private PermissionLevel level = PermissionLevel.NOT_ACCESSIBLE;
}
