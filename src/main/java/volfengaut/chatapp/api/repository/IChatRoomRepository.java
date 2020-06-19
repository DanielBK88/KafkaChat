package volfengaut.chatapp.api.repository;

import volfengaut.chatapp.entity.chat_room.ChatRoom;

/**
 * Interface for database operations concerning chat rooms
 **/
public interface IChatRoomRepository extends EntityManagerDependent {

    /**
     * Retrieves an existing chat room by it's name
     **/
    ChatRoom getChatRoom(String name);
    
    /**
     * Adds a new chat room with the specified name and creator
     **/
    void addChatRoom(ChatRoom chatRoom);
    
    /**
     * Deletes the specified chat room
     **/
    void deleteChatRoom(ChatRoom chatRoom);
}
