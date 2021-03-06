package volfengaut.chatapp.server.api.repository;

import volfengaut.chatapp.server.entity.role.UserRole;

/**
 * Interface for database operations concerning user roles
 **/
public interface IRoleRepository extends EntityManagerDependent {
    
    /**
     * Retrieves a user role by it's name
     **/
    UserRole getRoleByName(String name);

    /**
     * Removes the specified user role
     **/
    void deleteUserRole(UserRole role);

    /**
     * Adds a new user role with the specified name and permissions
     **/
    void addUserRole(UserRole role);
    
}
