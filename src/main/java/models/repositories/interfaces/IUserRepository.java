package models.repositories.interfaces;


import models.entities.User;

import java.util.List;

public interface IUserRepository {
    void add(User user);
    User getUserById(int UserId);
    void updateUser(User user);
    void deleteUser(int UserId);

    List<User> getUserList();
}
