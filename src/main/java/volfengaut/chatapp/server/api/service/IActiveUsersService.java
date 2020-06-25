package volfengaut.chatapp.server.api.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.user.User;

/**
 * A service managing active (connected) users, which are kept in memory.
 * Does not perform any database operations.
 **/
public interface IActiveUsersService {
    
    /**
     * Get a connected user by his login name
     **/
    User getActiveUserByName(String userName);
    
    /**
     * Get a connected user by the corresponding socket
     **/
    User getActiveUserBySocket(Socket socket);

    /**
     * Get the socket of a connected user
     */
    Socket getActiveUserSocket(String userName);
    
    /**
     * Add a new active user, who has just logged in or signed up
     **/
    void setActiveUser(Socket clientSocket, User user);

    /**
     * Remove an active user, who has gone offline
     **/
    void removeActiveUser(Socket clientSocket);

    /**
     * Get the names of chatters, who are currently participating the given chat room
     **/
    List<String> getNamesOfChattersInRoom(String roomName);

    /**
     * Store the information that the chatter entered the room
     **/
    void addChatterToRoom(ChatRoom room, String chatterName);
    
    /**
     * Removes the chatter with the given name from his current room
     **/
    void removeChatterFromHisRoom(String chatterName);
    
    /**
     * Get the object output stream to use for a client socket
     **/
    ObjectOutputStream getObjectOutputStreamForSocket(Socket socket);

    /**
     * Get the object input stream to use for a client socket
     **/
    ObjectInputStream getObjectInputStreamForSocket(Socket socket);
    
    /**
     * Get the address of the socket as <host-name>:<port-number>
     **/
    String getSocketAddress(Socket socket);
}
