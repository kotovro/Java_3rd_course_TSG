package models.repositories.implementations.inmemory;


import models.entities.User;
import models.repositories.interfaces.IUserRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



public class UserRepositoryInMemory implements IUserRepository {
    private List<User> users = new LinkedList<>();

    @Override
    public void add(User user) {
        int maxIndex = users.stream().max(Comparator.comparing(u -> u.getUserId())).get().getUserId();
        user.setUserId(maxIndex + 1);
        users.add(user);
    }

    @Override
    public User getUserById(int userId) {
        return users.stream().filter(u -> u.getUserId() == userId).findFirst().get();
    }

    @Override
    public void deleteUser(int userId) {
        users.removeIf(u -> u.getUserId() == userId);
    }

    @Override
    public List<User> getUserList() {
        return users;
    }

    @Override
    public void updateUser(User user) {
        User temp = getUserById(user.getUserId());
        temp.updateFromObject(user);
    }

}
