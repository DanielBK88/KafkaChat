package volfengaut.chatapp.message.client_to_server;

/**
 * Types of information messages sent by a client to the server
 **/
public enum ClientToServerMessageType {
    
    /**
     * Sends the entered login name to the server
     **/
    LOGIN_NAME,

    /**
     * Sends the entered password to the server
     **/
    PASSWORD,
    
    /**
     * Sends the login name for a new account to the server
     **/
    SIGN_UP_LOGIN_NAME,
    
    /**
     * Sends the password for a new account to the server
     **/
    SIGN_UP_PASSWORD_FIRST,

    /**
     * Sends the password confirmation for a new account to the server
     **/
    SIGN_UP_PASSWORD_SECOND,

    /**
     * Sends the selected room name to the server
     **/
    SELECTING_ROOM,
    
    /**
     * Sends a new room name to open and enter to the server
     **/
    OPENING_ROOM,
    
    /**
     * Informs the server, that the user is leaving the current chat room
     **/
    I_AM_LEAVING_THE_ROOM,
    
    /**
     * Informs the server, that the user is about to disconnect
     **/
    I_AM_DISCONNECTING;
}
