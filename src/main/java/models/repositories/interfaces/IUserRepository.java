package models.repositories.interfaces;


import models.entities.Role;
import models.entities.User;

import java.util.List;

public interface IUserRepository {
    int add(User user);
    User getUserById(int UserId);
    User authenticate(String login, String password);
    int updateUser(User user);
    void deleteUser(int userId);
    String getUserToken(int userId);
    void hashUserPassword(User user);
    List<User> getUserList();
    List<Role> getRoleList(int userId);
}
