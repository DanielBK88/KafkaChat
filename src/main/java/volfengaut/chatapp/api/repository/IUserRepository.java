package volfengaut.chatapp.api.repository;

import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;

/**
 * Interface for database operations concerning users
 **/
public interface IUserRepository {

    /**
     * Retrieve a user from the database by his login name
     **/
    User getUserByName(String loginName);
    
    /**
     * Delete the specified user
     **/
    boolean deleteUser(User user);
    
    /**
     * Add a new user with a specified name and user role
     **/
    User addUser(String name, UserRole role);
}
