package models.repositories.sql;

import models.entities.Resident;
import models.repositories.interfaces.IResidentRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ResidentRepositorySQL extends PostgreDBRepository implements IDataBaseConnection, IResidentRepository {

    public ResidentRepositorySQL(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public void add(Resident residentEntity) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into \"resident\"  (name, surname, street_name, home_number, active) values (?, ?, ?, ?, ?) returning  resident_id"     );
            statement.setString(1, residentEntity.getName());
            statement.setString(2, residentEntity.getSurname());
            statement.setString(3, residentEntity.getStreetName());
            statement.setString(4, residentEntity.getHomeNumber());
            statement.setBoolean(5, residentEntity.isActive());
            ResultSet resultSet = statement.executeQuery();
            int resId = 0;
            if (resultSet.next()) {
                resId = resultSet.getInt("resident_id");
            }
            residentEntity.setResidentId(resId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }

    @Override
    public Resident getResidentById(int residentId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        Resident resident = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"resident\" where resident_id = ? and active = true");
            statement.setInt(1, residentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int resId = resultSet.getInt("resident_id");
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String streetName = resultSet.getString("street_name");
                String homeNumber = resultSet.getString("home_number");
                boolean active = resultSet.getBoolean("active");
                resident = new Resident(resId, userId, name, surname, active, homeNumber, streetName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return resident;
    }

    @Override
    public void updateResident(Resident resident) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update \"resident\" set name=?, surname=?, street_name=?, house_number=?  where resident_id=?");
            statement.setString(1, resident.getName());
            statement.setString(2, resident.getSurname());
            statement.setString(3, resident.getStreetName());
            statement.setString(4, resident.getHomeNumber());
            statement.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        Resident temp = getResidentById(resident.getResidentId());
        temp.updateFromObject(resident);
    }

    @Override
    public void deleteResident(int residentId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update \"resident\" set  active=? where resident_id=? returning  resident_id");
            statement.setBoolean(1, false);
            statement.setInt(2, residentId);
            ResultSet resultSet = statement.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        Resident resident = getResidentById(residentId);
        if (resident == null) {
            return;
        }
        resident.setActive(false);
    }

    @Override
    public List<Resident> getAllResidents() {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        List<Resident> residents = new LinkedList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"resident\" where active = true");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int resId = resultSet.getInt("resident_id");
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String streetName = resultSet.getString("street_name");
                String homeNumber = resultSet.getString("home_number");
                boolean active = resultSet.getBoolean("active");
                Resident resident = new Resident(resId, userId, name, surname, active, homeNumber, streetName);
                residents.add(resident);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return residents;
    }

    @Override
    public String getNameByUserId(int userId) {
        String name = "N/A";
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return name;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"resident\" where user_id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (Exception e) {
        } finally {
            closeConnection(connection, statement);
        }
        return name;
    }

    @Override
    public String getNameById(int residentId) {
        String name = "N/A";
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return name;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"resident\" where resident_id = ?");
            statement.setInt(1, residentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("name");
            }
        }
        catch (Exception e) {
        } finally {
            closeConnection(connection, statement);
        }
        return name;
    }

}
