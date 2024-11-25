package models.repositories.sql;

import models.entities.Resident;
import models.repositories.interfaces.IResidentRepository;

import java.sql.*;

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
            ResultSet resultSet = statement.executeQuery();
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
