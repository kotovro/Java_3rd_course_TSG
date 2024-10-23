package models.repositories.implementations.inmemory;

import models.entities.StaffMember;
import models.repositories.interfaces.IStaffMemberRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class StaffMemberRepositoryInMemory implements IStaffMemberRepository {
    private List<StaffMember> staffMembers = new LinkedList<>();


    @Override
    public void add(StaffMember staffMember) {
        if (staffMembers != null && staffMembers.size() > 0) {
            int maxIndex = staffMembers.stream().max(Comparator.comparing(r -> r.getStaffMemberId())).get().getStaffMemberId();
            staffMember.setStaffMemberId(maxIndex + 1);
        }
        staffMember.setStaffMemberId(1);
        staffMembers.add(staffMember);
    }

    @Override
    public StaffMember getStaffMemberById(int staffMemberId) {
        return staffMembers.stream().filter(c -> c.getStaffMemberId() == staffMemberId).findFirst().get();
    }

    @Override
    public void updateStaffMember(StaffMember staffMember) {
        StaffMember temp = getStaffMemberById(staffMember.getStaffMemberId());
        temp.updateFromObject(staffMember);
    }

    @Override
    public void deleteStaffMember(int staffMemberId) {
        staffMembers.remove(staffMemberId);
    }

    @Override
    public String getNameByUserId(int userId) {
        try
        {
            StaffMember staffMember = staffMembers.stream().filter(c -> c.getUserId() == userId).findFirst().get();
            return staffMember.getName() + " " + staffMember.getSurname();
        }
        catch (NoSuchElementException e)
        {
            return "No such user";
        }
    }
}
