package models.repositories.sql;

import models.entities.Permission;
import models.entities.PermissionLevel;
import models.entities.Permissions;
import models.entities.Role;
import models.repositories.interfaces.IRoleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.LinkedList;
import java.util.List;
public class RoleRepositorySQL extends PostgreDBRepository implements IRoleRepository, IDataBaseConnection {

    public RoleRepositorySQL(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public Role getRoleById(int id) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        PreparedStatement statement = null;
        Role role = null;
        try {
            statement = connection.prepareStatement("select * from \"role\" WHERE role_id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = new Role();
                role.setId(id);
                role.setName(resultSet.getString("role_name"));

                PreparedStatement preparedStatement = connection.prepareStatement("select * from \"role_permission\" where role_id = ?");
                preparedStatement.setInt(1, role.getId());
                ResultSet resultSet2 = preparedStatement.executeQuery();

                while (resultSet2.next()) {
                    Permission permission = new Permission();
                    int permissionLevelId = resultSet2.getInt("permission_level_id");
                    permission.setLevel(PermissionLevel.name(permissionLevelId));
                    int permissionId = resultSet2.getInt("permission_name_id");
                    permission.setId(Permissions.name(permissionId));
                    role.getPermissions().add(permission);
                }


            }
        } catch (Exception e) {
            return role;
        } finally {
            closeConnection(connection, statement);
        }
        return role;
    }

    @Override
    public void addRole(Role role) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into \"role\"  (role_name) values (?) returning role_id");
            statement.setString(1, role.getName());
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) {
                return;
            }
            role.setId(resultSet.getInt("role_id"));
            resultSet.close();
            statement.close();
            List<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                statement = connection.prepareStatement("insert into \"role_permission\" (permission_name_id, permission_level_id, role_id) values (?, ?, ?)");
                statement.setInt(1, permission.getId().getId());
                statement.setInt(2, permission.getLevel().getPriority());
                statement.setInt(3, role.getId());
                statement.executeUpdate();
            }
        } catch (Exception e) {

        } finally {
            closeConnection(connection, statement);
        }
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = new LinkedList<>();
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return null;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select * from \"role\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("role_id"));
                role.setName(resultSet.getString("role_name"));
                roleList.add(role);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return roleList;
    }

    @Override
    public int updateRole(Role role) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return -1;
        }
        PreparedStatement statement = null;
        int roleId = -1;
        if (role.getId() == -1) {
            addRole(role);
            return role.getId();
        }
        try {
            statement = connection.prepareStatement("update \"role\" set role_name = ?  where role_id = ?");
            statement.setString(1, role.getName());
            statement.setInt(2, role.getId());
            roleId = statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
        return roleId;
    }

    @Override
    public void updateRolePermission(int roleId, Permission permission) {
        Connection connection = getConnection(url, username, password);
        if (connection == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update \"permission_role\" set permission_level_id = ? where role_id = ? and permission_name_id = ?");
            statement.setInt(1, permission.getLevel().getPriority());
            statement.setInt(2, roleId);
            statement.setInt(3, permission.getId().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }

}
