package volfengaut.chatapp.api.service;

import java.time.LocalDate;
import java.util.Collection;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;


/**
 * A service for working with user data
 **/
public interface IUserService {

    /**
     * Retrieve a user from the database by his login name
     **/
    User getUserByName(String loginName);

    /**
     * Delete the specified user
     **/
    void deleteUser(User user);

    /**
     * Add a new user with a specified name and user role
     **/
    void addUser(User user);
    
    /**
     * Retrieve a user by his role
     **/
    Collection<User> getUsersByRole(UserRole role);
    
    /**
     * Retrieve all users, who wrote messages containing the specified text
     **/
    Collection<User> getAuthorsOfMessagesContaining(String text);
    
    /**
     * Retrieve all users who signed up to the system after the specified date 
     **/
    Collection<User> getUsersJoinedAfter(LocalDate time);
    
    /**
     * Retrieve all users who were banned from the given chat room
     **/
    Collection<User> getChattersBannedFrom(ChatRoom room);
    
    /**
     * Retrieve all users who were banned by the specified user
     **/
    Collection<User> getChattersBannedBy(User user);
}
