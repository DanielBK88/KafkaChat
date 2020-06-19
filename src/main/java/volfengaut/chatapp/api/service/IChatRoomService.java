package volfengaut.chatapp.api.service;

import volfengaut.chatapp.entity.chat_room.ChatRoom;

/**
 * Used to manage chat rooms
 **/
public interface IChatRoomService {
    
    /**
     * Retrieves a chat room by it's name
     **/
    ChatRoom getChatRoom(String name);

    /**
     * Creates a new chat room
     **/
    void createChatRoom(ChatRoom room);
    
    /**
     * Deletes the specified chat room.
     **/
    void deleteChatRoom(ChatRoom room);
    
}
