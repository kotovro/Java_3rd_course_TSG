package models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Setter
    private int userId = -1;
    @Setter
    private String login;
    private String password;
    private byte[] passwordSalt;

    public User() {

    }

    public void setPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        byte[] hash = null;
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (Exception e) {

        }

        Base64.Encoder enc = Base64.getEncoder();
        this.password = enc.encodeToString(hash);
        this.passwordSalt = salt;
    }


    public void updateFromObject(User user) {
        this.setLogin(user.getLogin());
        this.setPassword(user.getPassword());
    }
}
