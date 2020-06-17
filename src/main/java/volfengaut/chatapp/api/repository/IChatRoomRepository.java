package volfengaut.chatapp.api.repository;

import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;

/**
 * Interface for database operations concerning chat rooms
 **/
public interface IChatRoomRepository {

    /**
     * Retrieves an existing chat room by it's name
     **/
    ChatRoom getChatRoom(String name);
    
    /**
     * Adds a new chat room with the specified name and creator
     **/
    ChatRoom addChatRoom(String name, User creator);
    
    /**
     * Deletes the specified chat room
     **/
    boolean deleteChatRoom(ChatRoom chatRoom);
}
