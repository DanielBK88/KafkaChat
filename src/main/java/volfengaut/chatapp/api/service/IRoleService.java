package volfengaut.chatapp.api.service;

import volfengaut.chatapp.entity.role.UserRole;

/**
 * Used to manage user roles
 **/
public interface IRoleService {

    /**
     * Retrieve a user role by it's name
     **/
    UserRole getRoleByName(String name);
    
    /**
     * Delete the specified user role
     **/
    void deleteUserRole(UserRole role);
    
    /**
     * Persist the specified user role
     **/
    void addUserRole(UserRole role);
}
