package models.repositories.interfaces;

import models.entities.Resident;

public interface IResidentRepository {
    void add(Resident resident);
    Resident getResidentById(int residentId);
    void updateResident(Resident resident);
    void deleteResident(int residentId);

    String getNameByUserId(int userId);

    String getNameById(int residentId);
}
