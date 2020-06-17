package volfengaut.chatapp.api.repository;

import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;

/**
 * Interface for database operations concerning user roles
 **/
public interface IRoleRepository {
    
    /**
     * Retrieves a user role by it's name
     **/
    UserRole getRoleByName(String name);

    /**
     * Removes the specified user role
     **/
    boolean deleteUserRole(UserRole role);

    /**
     * Adds a new user role with the specified name and permissions
     **/
    UserRole addUserRole(String name, Permisson... permissons);
    
}
