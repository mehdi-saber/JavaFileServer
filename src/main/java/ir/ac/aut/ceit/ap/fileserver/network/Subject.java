package ir.ac.aut.ceit.ap.fileserver.network;

public enum Subject {
    //    C->S Requests
    REGISTER_USER,
    LOGIN,
    RENAME_USER,
    RENAME_FILE,
    UPLOAD_FILE,
    REMOVE_FILE,
    MOVE_FILE,
    FETCH_DIRECTORY,


    //    S->C Response
    LOGIN_OK,
    LOGIN_FAILED,

    FETCH_DIRECTORY_OK,

    UPLOAD_FILE_OK


    }
