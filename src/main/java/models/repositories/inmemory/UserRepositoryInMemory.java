package models.repositories.inmemory;


import models.entities.Request;
import models.entities.Role;
import models.entities.User;
import models.repositories.interfaces.IUserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



public class UserRepositoryInMemory implements IUserRepository {
    private List<User> users = new LinkedList<>();

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

    private int getUserIdFromToken(String token) {
        return Integer.parseInt(token);
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
    public User authenticate(String login, String password) {
        User usr = new User();
        try {
            User tmp = users.stream().filter(u -> u.getLogin().equals(login)).findFirst().get();
            String hash = hashPassword(password, tmp.getPasswordSalt());
            if (hash.equals(tmp.getPassword())) {
                usr = tmp;
            }
        } catch (Exception e) {

        }
        return usr;
    }

    @Override
    public void deleteUser(int userId) {
        users.removeIf(u -> u.getUserId() == userId);
    }

    @Override
    public String getUserToken(int userId) {
        return Integer.toString(userId);
    }

    @Override
    public List<User> getUserList() {
        return users;
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public List<Role> getRoleList(int userId) {
        return List.of();
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
