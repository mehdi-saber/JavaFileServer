package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum  C2SRequest implements RequestSubject{
    LOGIN,
    RENAME_FILE,
    UPLOAD_FILE,
    Preview,
    CREATE_NEW_DIRECTORY,
    FETCH_DIRECTORY,
    DELETE_FILE,
    FILE_DIST,
    PASTE
}
