package models.repositories.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IDataBaseConnection {
    default Connection getConnection(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url,
                        user, password);
        } catch (Exception e) {
            return null;
        }
    }

    default void closeConnection(Connection connection, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
        }
        try {
            connection.close();
        } catch (Exception e) {
        }
    }


}
