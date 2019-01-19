package ir.ac.aut.ceit.ap.fileserver.network;

public enum Subject {
    //    C->S Requests
    REGISTER_USER,
    LOGIN,
    RENAME_USER,
    RENAME_FILE,
    UPLOAD_FILE,
    REMOVE_FILE,
    MOVE_PATH,
    DOWNLOAD_FILE,
    CREATE_NEW_DIRECTORY,
    FETCH_DIRECTORY,


    //    S->C Response
    LOGIN_OK,
    LOGIN_FAILED,
    FETCH_DIRECTORY_OK,
    UPLOAD_FILE_OK,
    CREATE_NEW_DIRECTORY_REPEATED,
    CREATE_NEW_DIRECTORY_OK,
    RENAME_PATH_REPEATED,
    RENAME_PATH_OK,
    MOVE_PATH_ALREADY_EXISTS,
    MOVE_PATH_OK,
    DOWNLOAD_FILE_OK,


    //    S->C requests
    FETCH_PART,
    REFRESH_DIRECTORY,

    //    C->S responses
    FETCH_PART_OK,
    REFRESH_DIRECTORY_OK,
}
