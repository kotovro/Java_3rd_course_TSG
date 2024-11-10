package models.repositories.interfaces;


import models.entities.User;

import java.util.List;

public interface IUserRepository {
    int add(User user);
    User getUserById(int UserId);
    User getUserByLogin(String login);
    int updateUser(User user);
    void deleteUser(int UserId);

    List<User> getUserList();
}
