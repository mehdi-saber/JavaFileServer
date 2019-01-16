package ir.ac.aut.ceit.ap.fileserver.server.access;

import ir.ac.aut.ceit.ap.fileserver.filesys.PathInfo;

import java.util.Map;

public class User {
    String username;
    Map<PathInfo, PermissionType> permissionMap;
}
