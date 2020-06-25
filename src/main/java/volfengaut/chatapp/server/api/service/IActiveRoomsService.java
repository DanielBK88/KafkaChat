package volfengaut.chatapp.server.api.service;

import java.util.List;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;

/**
 * A service providing the information about active (non-empty) rooms, which are kept in memory.
 * Does not perform any database operation.
 **/
public interface IActiveRoomsService {

    /**
     * Get the names of currently active (non-empty) rooms
     **/
    List<String> getNamesOfActiveRooms();
    
    /**
     * Returns the active room with the specified name, if it exists
     **/
    ChatRoom getActiveRoom(String roomName);
    
    /**
     * Add a new active room to the list (created by a chatter)
     **/
    void addActiveRoom(ChatRoom room);
    
    /**
     * Remove a room from the active rooms list (left by all chatters or destroyed by an admin)
     **/
    void removeActiveRoom(ChatRoom room);
    
}
