package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum  C2SRequest implements RequestSubject{
    LOGIN,
    RENAME_FILE,
    UPLOAD_FILE,
    DOWNLOAD_FILE,
    CREATE_NEW_DIRECTORY,
    FETCH_DIRECTORY,
    PASTE
}
