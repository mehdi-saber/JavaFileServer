package ir.ac.aut.ceit.ap.fileserver.model.network;

import java.io.Serializable;

public class AuthDto implements Serializable {
    private String username;
    private String password;

    public AuthDto(String username, String password) {
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
