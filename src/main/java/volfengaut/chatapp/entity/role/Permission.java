package volfengaut.chatapp.entity.role;

/**
 * Permissions, which may be included in different user roles
 **/
public enum Permission {
    
    /**
     * Permission to send public messages within a chat room
     **/
    SEND_PUBLIC_MESSAGES,

    /**
     * Permission to send private messages within a chat room
     **/
    SEND_PRIVATE_MESSAGES,

    /**
     * Permission to create new chat rooms
     **/
    CREATE_CHAT_ROOM,

    /**
     * Permission to delete chat rooms
     **/
    DELETE_CHAT_ROOM, //TODO: Not yet implemented!

    /**
     * Permission to ban a chatter from a chat room
     **/
    REMOVE_CHATTER_FROM_CHAT_ROOM,
    
    /**
     * Permission to delete a chatter account
     **/
    REMOVE_CHATTER_FROM_SYSTEM; //TODO: Not yet implemented!
}
