package ir.ac.aut.ceit.ap.fileserver.network;

public enum Subject {
    //    C->S Requests
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


    //    S->C Response
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


    //    S->C requests
    RECEIVE_PART,
    SEND_PART,
    REFRESH_DIRECTORY,

    //    C->S responses
    RECEIVE_PART_OK,
    SEND_PART_OK,
    REFRESH_DIRECTORY_OK,
}
