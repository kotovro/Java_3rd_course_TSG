package models.repositories.inmemory;

import models.entities.Contractor;
import models.repositories.interfaces.IContractorRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ContractorRepositoryInMemory implements IContractorRepository {
    private List<Contractor> contractors = new LinkedList<>();

    @Override
    public void add(Contractor contractor) {
        int maxIndex = this.contractors.stream().max(Comparator.comparing(c -> c.getContactorId())).get().getContactorId();
        contractor.setContactorId(maxIndex);
        contractors.add(contractor);
    }

    @Override
    public Contractor getContractorById(int contractorId) {
        return contractors.stream().filter(c -> c.getContactorId() == contractorId).findFirst().get();
    }

    @Override
    public void updateContractor(Contractor contractor) {
        Contractor temp = getContractorById(contractor.getContactorId());
        temp.updateFromObject(contractor);
    }

    @Override
    public void deleteContractor(int contractorId) {
        contractors.removeIf(c -> c.getContactorId() == contractorId);
    }
}
