package models.repositories.interfaces;

import models.entities.Contractor;
import models.entities.User;

public interface IContractorRepository {
    void add(Contractor Contractor);
    Contractor getContractorById(int ContractorId);
    void updateContractor(Contractor Contractor);
    void deleteContractor(int ContractorId);
}
