package volfengaut.chatapp.api.service;

import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;

/**
 * Used to manage chat rooms
 **/
public interface IChatRoomService {
    
    /**
     * Retrieves a chat room by it's name
     **/
    ChatRoom getChatRoom(String name);

    /**
     * Creates a new chat room (and a corresponding Kafka topic), if it does not yet exist.
     * @param name the name of the new chat room
     * @param creator the 
     * @return true, if the chat room (topic) did not yet exist.
     **/
    ChatRoom createChatRoom(String name, User creator);
    
    /**
     * Deletes the specified chat room.
     **/
    boolean deleteChatRoom(ChatRoom room);
    
}
