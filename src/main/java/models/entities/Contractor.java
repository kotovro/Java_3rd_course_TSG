package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Contractor {

    private int contactorId;
    private String contractorName;

    public void updateFromObject(Contractor contractor)
    {
        contractor.setContractorName(contractor.contractorName);
    }
}
