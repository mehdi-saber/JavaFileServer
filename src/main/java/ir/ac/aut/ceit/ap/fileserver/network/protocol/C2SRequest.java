package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum  C2SRequest implements RequestSubject{
    REGISTER_USER,
    LOGIN,
    RENAME_USER,
    RENAME_FILE,
    UPLOAD_FILE,
    DOWNLOAD_FILE,
    REMOVE_FILE,
    MOVE_PATH,
    CREATE_NEW_DIRECTORY,
    FETCH_DIRECTORY,
    PASTE
}
