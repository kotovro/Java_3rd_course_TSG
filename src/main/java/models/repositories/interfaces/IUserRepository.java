package models.repositories.interfaces;


import models.entities.User;

import java.util.List;

public interface IUserRepository {
    int add(User user);
    User getUserById(int UserId);
    User getUserByToken(String token);
    User authenticate(String login, String password);
    int updateUser(User user);
    void deleteUser(int userId);
    String getUserToken(int userId);
    void hashUserPassword(User user);
    List<User> getUserList();
}
