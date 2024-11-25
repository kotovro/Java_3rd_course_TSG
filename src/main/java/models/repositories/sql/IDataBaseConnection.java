package models.repositories.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IDataBaseConnection {
    default Connection getConnection(String url, String user, String password) {
        try {
        return DriverManager.getConnection(url,
                        user, password);
        } catch (SQLException e) {
            return null;
        }
    }

    default void closeConnection(Connection connection, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
        }
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    default void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

}
