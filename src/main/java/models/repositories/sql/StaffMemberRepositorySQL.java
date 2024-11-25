package models.repositories.sql;

import models.entities.StaffMember;
import models.repositories.interfaces.IStaffMemberRepository;

import java.sql.*;

public class StaffMemberRepositorySQL extends PostgreDBRepository implements IDataBaseConnection, IStaffMemberRepository {


    public StaffMemberRepositorySQL(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public void add(StaffMember staffMember) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into staff_member (name, surname, user_id, active) values(?,?,?,?) returning staff_member_id");
            preparedStatement.setString(1, staffMember.getName());
            preparedStatement.setString(2, staffMember.getSurname());
            preparedStatement.setInt(3, staffMember.getUserId());
            preparedStatement.setBoolean(4, staffMember.isActive());
            ResultSet resultSet = preparedStatement.executeQuery();
            int staffMemberId = 0;
            if (resultSet.next()) {
                staffMemberId = resultSet.getInt("staff_member_id");
                staffMember.setStaffMemberId(staffMemberId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public StaffMember getStaffMemberById(int staffMemberId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        StaffMember staffMember = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from staff_member where staff_member_id=?");
            statement.setInt(1, staffMemberId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int userId = resultSet.getInt("user_id");
                boolean active = resultSet.getBoolean("active");
                staffMember = new StaffMember(staffMemberId, userId, name, surname, active);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return staffMember;
    }

    @Override
    public void updateStaffMember(StaffMember staffMember) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("update staff_member set name=?, surname=? where staff_member_id=?");
            preparedStatement.setString(1, staffMember.getName());
            preparedStatement.setString(2, staffMember.getSurname());
            preparedStatement.setBoolean(3, staffMember.isActive());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public void deleteStaffMember(int staffMemberId) {

    }

    @Override
    public String getNameByUserId(int userId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        String name = "";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select name from staff_member where user_id=?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, preparedStatement);
        }
        return name;
    }

}
