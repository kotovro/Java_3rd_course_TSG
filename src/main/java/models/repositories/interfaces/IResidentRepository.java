package models.repositories.interfaces;

import models.entities.Resident;

import java.util.List;

public interface IResidentRepository {
    void add(Resident resident);
    Resident getResidentById(int residentId);
    void updateResident(Resident resident);
    void deleteResident(int residentId);
    List<Resident> getAllResidents();
    String getNameByUserId(int userId);

    String getNameById(int residentId);
}
