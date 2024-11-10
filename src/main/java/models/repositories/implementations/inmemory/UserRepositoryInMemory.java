package models.repositories.implementations.inmemory;


import models.entities.Request;
import models.entities.User;
import models.repositories.interfaces.IUserRepository;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



public class UserRepositoryInMemory implements IUserRepository {
    private List<User> users = new LinkedList<>();

    @Override
    public int add(User user) {
        int maxIndex = users.size() == 0 ? 0
                : users.stream().max(Comparator.comparing(r -> r.getUserId())).get().getUserId();
        user.setUserId(maxIndex + 1);
        users.add(user);
        return maxIndex + 1;
    }

    @Override
    public User getUserById(int userId) {
        return users.stream().filter(u -> u.getUserId() == userId).findFirst().get();
    }

    @Override
    public User getUserByLogin(String login) {
        User usr = null;
        try {
            usr = users.stream().filter(u -> u.getLogin().equals(login)).findFirst().get();
        } catch (Exception e) {

        }
        return usr;
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
    public int updateUser(User user) {
        if (user.getUserId() == -1)
        {
            add(user);
        } else {
            User temp = getUserById(user.getUserId());
            temp.updateFromObject(user);
        }
        return user.getUserId();
    }

}
