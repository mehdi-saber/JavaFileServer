package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum S2CResponse implements ResponseSubject {
    LOGIN_OK,
    LOGIN_FAILED,
    FETCH_DIRECTORY_OK,
    UPLOAD_FILE_OK,
    UPLOAD_FILE_REPEATED,
    CREATE_NEW_DIRECTORY_REPEATED,
    CREATE_NEW_DIRECTORY_OK,
    RENAME_PATH_REPEATED,
    RENAME_PATH_OK,
    MOVE_PATH_ALREADY_EXISTS,
    MOVE_PATH_OK,
    DOWNLOAD_FILE_OK,
}
