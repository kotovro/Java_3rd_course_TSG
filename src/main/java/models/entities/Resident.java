package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Resident {

    private int residentId;
    private int userId;
    private String name;
    private String surname;
    private boolean isActive;
    private String homeNumber;
    private String streetName;

    public void updateFromObject(Resident resident) {
        this.setStreetName(resident.getStreetName());
        this.setHomeNumber(resident.getHomeNumber());
        this.setName(resident.getName());
        this.setSurname(resident.getSurname());
        this.setActive(resident.isActive());
    }
}
