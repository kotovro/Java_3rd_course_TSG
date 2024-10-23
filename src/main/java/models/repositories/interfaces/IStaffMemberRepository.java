package models.repositories.interfaces;

import models.entities.StaffMember;

public interface IStaffMemberRepository {
    void add(StaffMember staffMember);
    StaffMember getStaffMemberById(int staffMemberId);
    void updateStaffMember(StaffMember staffMember);
    void deleteStaffMember(int staffMemberId);

    String getNameByUserId(int userId);
}
