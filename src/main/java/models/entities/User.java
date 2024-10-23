package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private int userId;
    private int status;
    private String login;
    private String password;

    public void updateFromObject(User user) {
        this.setStatus(user.getStatus());
        this.setLogin(user.getLogin());
        this.setPassword(user.getPassword());
    }
}
