package models.repositories.sql;

import models.entities.Role;
import models.entities.User;
import models.repositories.interfaces.IUserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

public class UserRepositorySQL extends PostgreDBRepository implements IDataBaseConnection, IUserRepository {

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
        PreparedStatement statementAddUser = null;
        try {
            statementAddUser = connection.prepareStatement("INSERT INTO \"user\" (\"login\", \"password\", password_salt, active) VALUES (?, ?, ?, ?) returning user_id");
            statementAddUser.setString(1, user.getLogin());
            statementAddUser.setString(2, user.getPassword());
            statementAddUser.setBytes(3, user.getPasswordSalt());
            statementAddUser.setBoolean(4, user.isActive());
            ResultSet resultSet = statementAddUser.executeQuery();
            if (resultSet.next()) {
                usrId = resultSet.getInt("user_id");
                user.setUserId(usrId);

                if (!user.getRoles().isEmpty()) {
                    PreparedStatement statementAddUserRoles = getPreparedStatementForInsert(user.getRoles(), connection, usrId);
                    statementAddUserRoles.executeUpdate();
                    statementAddUserRoles.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statementAddUser);
        }
        return usrId;
    }

    private static PreparedStatement getPreparedStatementForInsert(List<Integer> roles, Connection connection, int usrId) throws SQLException {
        StringBuilder queryAddUserRoles = new StringBuilder("insert into user_role (user_id, role_id) values ");
        StringBuilder queryValues = new StringBuilder("(?, ?)");

        for (int i = 1; i < roles.size(); i++) {
            queryValues.append(", (?, ?)");
        }

        queryAddUserRoles.append(queryValues);
        PreparedStatement statementAddUserRoles = connection.prepareStatement(queryAddUserRoles.toString());
        for (int i = 0; i < roles.size(); i++) {
            statementAddUserRoles.setInt(1 + i * 2, usrId);
            statementAddUserRoles.setInt(2 + i * 2, roles.get(i));
        }
        return statementAddUserRoles;
    }


    @Override
    public User getUserById(int UserId) {
        User user = null;
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"user\" where \"user\".user_id=?");
            statement.setInt(1, UserId);
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
                statementForRoles.setInt(1, UserId);
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
    public User authenticate(String login, String password) {
        Connection connection = getConnection(this.url, this.username, this.password);
        User usr = new User();
        if (connection == null) {
            return usr;
        }

        PreparedStatement statementAddUser = null;
        try {
            statementAddUser = connection.prepareStatement("select * from \"user\" where \"user\".login=? ");
            statementAddUser.setString(1, login);
            ResultSet resultSet = statementAddUser.executeQuery();
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
            closeConnection(connection, statementAddUser);
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
            User oldUser = getUserById(userId);

            if (!user.getLogin().equals(oldUser.getLogin()) ||
                    !user.getPassword().equals(oldUser.getPassword()) ||
                    !Arrays.equals(user.getPasswordSalt(), oldUser.getPasswordSalt())) {
                statement = connection.prepareStatement("update \"user\" set login=?, password=?, password_salt=? where user_id=?");
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPassword());
                statement.setBytes(3, user.getPasswordSalt());
                statement.setInt(4, user.getUserId());
                statement.executeUpdate();
                statement.close();
            }
            Set<Integer> oldUserRoles = new HashSet<Integer>(oldUser.getRoles());
            Set<Integer> newUserRoles = new HashSet<Integer>(user.getRoles());
            Set<Integer> rolesToDelete = oldUserRoles.stream()
                    .filter(e -> !newUserRoles.contains(e))
                    .collect(Collectors.toSet());
            Set<Integer> rolesToInsert = newUserRoles.stream()
                    .filter(e -> !oldUserRoles.contains(e))
                    .collect(Collectors.toSet());
            if (!rolesToDelete.isEmpty()) {
                StringBuilder deleteRoles = new StringBuilder("?");
                for (int i = 1; i < rolesToDelete.size(); i++) {
                    deleteRoles.append(", ?");
                }

                PreparedStatement statementDeleteRoles = connection.prepareStatement(
                        "delete from \"user_role\" where user_id=? and role_id in (" + deleteRoles + ")");
                statementDeleteRoles.setInt(1, userId);
                List<Integer> dr = rolesToDelete.stream().toList();
                for (int i = 0; i < rolesToDelete.size(); i++) {
                    statementDeleteRoles.setInt(2 + i, dr.get(i));
                }
                statementDeleteRoles.executeUpdate();
                statementDeleteRoles.close();
            }

            if (!rolesToInsert.isEmpty()) {
                PreparedStatement statementInsertRoles = getPreparedStatementForInsert(
                        rolesToInsert.stream().toList(), connection, userId);
                statementInsertRoles.executeUpdate();
                statementInsertRoles.close();
            }
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

        } finally {
            closeConnection(connection, statement);
        }
        return userList;
    }

    @Override
    public List<Role> getRoleList(int userId) {
        Connection connection = getConnection(url, username, password);
        List<Role> roleList = new LinkedList<>();
        if (connection == null) return null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "select role.role_name, role.role_id from user_role join role on user_role.role_id = role.role_id  where user_id =?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setName(resultSet.getString("role_name"));
                role.setId(resultSet.getInt("role_id"));
                roleList.add(role);
            }
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
        return roleList;
    }

}
