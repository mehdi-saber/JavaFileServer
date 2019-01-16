package ir.ac.aut.ceit.ap.fileserver.network;

public enum ExchangeTitle {
    //    C->S Requests
    REGISTER_USER,
    LOGIN_USER,
    RENAME_USER,
    RENAME_FILE,
    UPLOAD_FILE,
    REMOVE_FILE,
    MOVE_FILE,


    //    S->C Response
//    REGISTER_USER
    REGISTER_USER_PASS_NOT_ACCEPTED,
    REGISTER_USER_REPEATED_USERNAME,
    REGISTER_USER_ACCEPTED,
//    LOGIN_USER

}