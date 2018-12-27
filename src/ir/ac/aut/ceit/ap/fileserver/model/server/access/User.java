package ir.ac.aut.ceit.ap.fileserver.model.server.access;

import ir.ac.aut.ceit.ap.fileserver.model.filesys.AddressInfo;

import java.util.Map;

public class User {
    String username;
    Map<AddressInfo, PermissionType> permissionMap;
}
