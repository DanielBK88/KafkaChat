package volfengaut.chatapp.message.server_to_client;

/**
 * Types of request messages send by the server to clients
 **/
public enum ServerToClientMessageType {
    
    /**
     * Requests the client to enter his login name
     **/
    PLEASE_ENTER_NAME,
    
    /**
     * Informs the client, that the specified name is not yet registered
     * and he should either sign up or enter a new name
     **/
    UNKNOWN_NAME,
    
    /**
     * Informs the client, that the specified name is either already taken
     * or does not match the criteria.
     **/
    NAME_NOT_AVAILABLE,
    
    /**
     * Requests the client to enter his password
     **/
    PLEASE_ENTER_PASSWORD,
    
    /**
     * Informs the client, that the password he entered is not correct
     **/
    INVALID_PASSWORD,
    
    /**
     * Requests the client to enter a new password during the sign-up process
     **/
    SELECT_NEW_PASSWORD,
    
    /**
     * Informs the client, that the new password he has chosen does not match the password criteria
     **/
    NEW_PASSWORD_DOES_NOT_MATCH_CRITERIA,

    /**
     * Requests the client to enter his new password a second time for confirmation
     **/
    CONFIRM_NEW_PASSWORD,
    
    /**
     * Informs the client, that the password sent as confirmation differs from the password he has sent the first time.
     **/
    PASSWORD_CONFIRMATION_FAILED,
    
    /**
     * Requests the client to select a chat room
     **/
    SELECT_CHAT_ROOM,
    
    /**
     * Informs the client that the chat room he has chosen does not yet exist
     **/
    UNKNOWN_CHAT_ROOM,
    
    /**
     * Informs the client that he was trying to open a chat room, but this is not allowed by his role.
     **/
    CHAT_ROOM_OPENING_NOT_ALLOWED,
    
    /**
     * Informs the client about existing other chatters in the chat room he entered.
     **/
    EXISTING_CHATTERS_INFO,
    
    /**
     * Informs the client, that some chatter entered his chat room 
     **/
    CHATTER_ENTERED,
    
    /**
     * Informs the client, that some chatter left his chat room
     **/
    CHATTER_LEFT,
    
    /**
     * Informs the client, who has tried to ban a chatter, that, according to his role, he has no permission to do so.
     **/
    NO_PERMISSION_TO_BAN_USERS;
}
