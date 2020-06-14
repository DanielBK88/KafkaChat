package volfengaut.chatapp.message;

import java.io.Serializable;

/**
 * Enumeration of possible basic status chatter changes, which cause an info message being send to other chatters.
 **/
public enum ChatterStatusChange implements Serializable {
    
    /**
     * A new chatter enters the chat room
     **/
    ENTERING, 
    
    /**
     * A chatter leaves the chat room
     **/
    LEAVING;
}
