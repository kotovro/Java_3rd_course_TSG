package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private int userId;
    private String login;
    private String password;
    private boolean active = true;
    private byte[] passwordSalt = new byte[16];
    private List<Integer> roles = new LinkedList<>();


    public User() {
    }

    public void updateFromObject(User user) {
        this.setLogin(user.getLogin());
        this.setPassword(user.getPassword());
        this.setActive(user.isActive());
        this.setPasswordSalt(user.getPasswordSalt());
        this.setRoles(user.getRoles());
    }
}
