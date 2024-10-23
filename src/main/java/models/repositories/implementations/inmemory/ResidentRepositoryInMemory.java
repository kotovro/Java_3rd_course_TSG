package models.repositories.implementations.inmemory;


import models.entities.Resident;
import models.repositories.interfaces.IResidentRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class ResidentRepositoryInMemory implements IResidentRepository {
    private List<Resident> residents = new LinkedList<>();

    @Override
    public void add(Resident resident) {
        if (residents != null && residents.size() > 0) {
            int maxIndex = residents.stream().max(Comparator.comparing(r -> r.getResidentId())).get().getResidentId();
            resident.setResidentId(maxIndex + 1);
        }
        resident.setResidentId(1);
        residents.add(resident);
    }

    @Override
    public Resident getResidentById(int residentId) {
        return residents.stream().filter(r -> r.getResidentId() == residentId).findFirst().get();
    }

    @Override
    public void updateResident(Resident resident) {
        Resident temp = getResidentById(resident.getResidentId());
        temp.updateFromObject(resident);
    }

    @Override
    public void deleteResident(int residentId) {
        residents.removeIf(r -> r.getResidentId() == residentId);
    }

    @Override
    public String getNameByUserId(int userId) {
        Resident resident = residents.stream().filter(r -> r.getUserId() == userId).findFirst().get();
        return resident.getName() + " " + resident.getSurname();
    }

    @Override
    public String getNameById(int residentId) {
        return residents.stream()
                .filter(r -> r.getResidentId() == residentId)
                .map(r -> r.getSurname() + " " + r.getName())
                .findFirst()
                .get();
    }


}
