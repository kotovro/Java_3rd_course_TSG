package services;

import models.entities.User;
import models.repositories.interfaces.IUserRepository;

public class UserService {

    private IUserRepository userRepository;

    public void addUser(User user)
    {
        userRepository.add(user);
    }

    public User getUser(int userId)
    {
        return userRepository.getUserById(userId);
    }

}
