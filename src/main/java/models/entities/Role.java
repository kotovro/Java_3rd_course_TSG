package models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Role {
    private int id;
    private String name;
    private List<Permission> permissions;

    public Role() {}
//    public Role(List<Permissions> permissions) {
//        this.permissions = permissions;
//    }

    public void updateFromObject(Role role) {
        role.setName(this.name);
        role.setPermissions(this.permissions);
    }
}
