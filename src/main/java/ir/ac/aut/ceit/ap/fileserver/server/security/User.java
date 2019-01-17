package ir.ac.aut.ceit.ap.fileserver.server.security;

import ir.ac.aut.ceit.ap.fileserver.file.FSPath;

import java.util.Map;

public class User {
    private String username;
    private String passHash;
    private Map<FSPath, PermissionType> permissionMap;

    public User(String username, String passHash) {
        this.username = username;
        this.passHash = passHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPassHash() {
        return passHash;
    }

    public Map<FSPath, PermissionType> getPermissionMap() {
        return permissionMap;
    }
}
