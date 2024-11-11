package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private int userId = -1;
    private String login;
    private String password;
    private byte[] passwordSalt;

    public User() {
    }

    public void updateFromObject(User user) {
        this.setLogin(user.getLogin());
        this.setPassword(user.getPassword());
        this.setPasswordSalt(user.getPasswordSalt());
    }
}
