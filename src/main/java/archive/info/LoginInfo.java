package archive.info;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private final String username;
    private final String password;

    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
