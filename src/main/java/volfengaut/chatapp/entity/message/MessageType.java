package volfengaut.chatapp.entity.message;

/**
 * A list of possible types of messages
 **/
public enum MessageType {
    
    /**
     * A usual chat message
     **/
    CHAT, 
    
    /**
     * A message informing about a status change of the author
     **/
    STATUS_CHANGE, 
    
    /**
     * A message informing new entering room participants about already existing participants
     **/
    EXISTING_CHATTER_INFO, 
    
    /**
     * A massage to ban a chatter from a chat room
     **/
    BAN;
}
