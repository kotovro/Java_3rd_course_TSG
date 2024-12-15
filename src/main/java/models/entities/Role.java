package models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Role {
    private int id = -1;
    private String name = "New role";
    private List<Permission> permissions = new LinkedList<>();

    public Role() {}

    public void updateFromObject(Role role) {
        role.setName(this.name);
        role.setPermissions(this.permissions);
    }
}
