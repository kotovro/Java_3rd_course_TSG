package models.repositories.sql;

import models.entities.User;
import models.repositories.interfaces.IUserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class UserRepositorySQL extends PostgreDBRepository implements IDataBaseConnection, IUserRepository {

    private ResultSet resultSet;

    public UserRepositorySQL(String url, String user, String password) {
        super(url, user, password);
    }


    private String hashPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        byte[] hash = null;
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hash);
    }

    @Override
    public int add(User user) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return -1;
        }
        int usrId = -1;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO \"user\" (login, password, password_salt) VALUES (?, ?, ?) returning user_id");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setBytes(3, user.getPasswordSalt());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                usrId = resultSet.getInt("user_id");
                user.setUserId(usrId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return usrId;
    }



    @Override
    public User getUserById(int UserId) {
        User user = null;
        Connection connection = getConnection(url, username, password);
        if (connection == null) {return null;}
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"user\"  where \"user\".user_id=?");
            statement.setInt(1,UserId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
            }
            if (resultSet.next()) {
                user = new User();
                user.setUserId(UserId);
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setPasswordSalt(resultSet.getBytes("password_salt"));
                List<Integer> userRoles = new LinkedList<>();
                PreparedStatement statementForRoles = connection.prepareStatement("select role_id from \"user_role\" where user_id=?");
                statementForRoles.setInt(1,UserId);
                ResultSet resultSetForRoles = statementForRoles.executeQuery();
                while (resultSetForRoles.next()) {
                    userRoles.add((resultSetForRoles.getInt("role_id")));
                }
                user.setRoles(userRoles);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return user;
    }

    @Override
    public User getUserByToken(String token) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) return null;
        PreparedStatement statement = null;
        User user = new User();
        try {
            statement = connection.prepareStatement("select * from \"user\"  where user_id=?");
            statement.setInt(1, getUserIdFromToken(token));
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return user;
            }
            user.setUserId(resultSet.getInt("user_id"));
            user.setPasswordSalt(resultSet.getBytes("password_salt"));
            user.setPassword(resultSet.getString("password"));
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user_role where user_id=?");
            preparedStatement.setInt(1, getUserIdFromToken(token));
            ResultSet resultSetForRoles = preparedStatement.executeQuery();
            List<Integer> userRoles = new LinkedList<>();
            while (resultSetForRoles.next()) {
                userRoles.add((resultSetForRoles.getInt("role_id")));
            }
            user.setRoles(userRoles);
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
        return user;
    }


    @Override
    public User authenticate(String login, String password) {
        Connection connection = getConnection(this.url, this.username, this.password);
        User usr = new User();
        if (connection == null) {
            return usr;
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"user\" where \"user\".login=? ");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return usr;
            }

            String userPassword = resultSet.getString("password");
            byte[] salt = resultSet.getBytes("password_salt");
            String hash = hashPassword(password, salt);
            if (hash.equals(userPassword)) {
                usr.setUserId(resultSet.getInt("user_id"));
                usr.setLogin(resultSet.getString("login"));
                usr.setPassword(resultSet.getString("password"));
                usr.setPasswordSalt(resultSet.getBytes("password_salt"));
                PreparedStatement statementForRoles = connection.prepareStatement("select role_id from \"user_role\" where user_id=?");
                statementForRoles.setInt(1, usr.getUserId());
                ResultSet resultSetForRoles = statementForRoles.executeQuery();
                usr.setRoles(new LinkedList<>());
                List<Integer> userRoles = new LinkedList<>();
                while (resultSetForRoles.next()) {
                    userRoles.add((resultSetForRoles.getInt("role_id")));
                }
                resultSetForRoles.close();
                usr.setRoles(userRoles);
            }
        } catch (Exception e) {
            return usr;

        } finally {
            closeConnection(connection, statement);
        }
        return usr;
    }

    @Override
    public int updateUser(User user) {
        int userId = user.getUserId();
        Connection connection = getConnection(url, username, password);
        if (connection == null) return userId;
        if (userId == -1) {
            userId = add(user);
            return userId;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update \"user\" set login=?, password=?, password_salt=? where user_id=?");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setBytes(3, user.getPasswordSalt());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return userId;
    }

    @Override
    public void deleteUser(int userId) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) return;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update \"user\" set active=false where user_id=?");
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
    }
    private int getUserIdFromToken(String token) {
        return Integer.parseInt(token);
    }
    @Override
    public String getUserToken(int userId) {
        return Integer.toString(userId);
    }

    @Override
    public void hashUserPassword(User user) {
        byte[] salt = user.getPasswordSalt();
        if (salt == null) {
            salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            user.setPasswordSalt(salt);
        }
        String hashPassword = hashPassword(user.getPassword(), salt);
        user.setPassword(hashPassword);
    }

    @Override
    public List<User> getUserList() {
        Connection connection = getConnection(url, username, password);
        if (connection == null) return null;
        PreparedStatement statement = null;
        List<User> userList = new LinkedList<>();
        try {
            statement = connection.prepareStatement("select * from \"user\" where active=true");
            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setLogin(resultSet.getString("login"));
                userList.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return userList;
    }
}
