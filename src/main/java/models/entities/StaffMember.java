package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class StaffMember {
    private int staffMemberId = -1;
    private int userId = -1;
    private String name = "";
    private String surname = "";
    private boolean isActive = false;

    public void updateFromObject(StaffMember staffMember) {
        this.setName(staffMember.getName());
        this.setSurname(staffMember.getSurname());
        this.setActive(staffMember.isActive());
    }
}
