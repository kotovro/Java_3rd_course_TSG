package models.repositories.inmemory;

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
        int maxIndex = staffMembers.isEmpty() ? 0
                : staffMembers.stream().max(Comparator.comparing(s -> s.getStaffMemberId())).get().getStaffMemberId();
        staffMember.setUserId(maxIndex + 1);
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
