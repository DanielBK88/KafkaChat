package volfengaut.chatapp.api;

/**
 * Used to manage chat rooms
 **/
public interface IChatRoomService {

    /**
     * Creates a new chat room (i.e. a Kafka topic), if a topic with the specified name does not yet exist.
     * @param name the name of the new chat room
     * @return true, if the chat room (topic) did not yet exist.
     **/
    boolean createChatRoom(String name);
    
}
