package volfengaut.chatapp.server.api.service;

import volfengaut.chatapp.server.entity.role.UserRole;

/**
 * A service used to cache frequently used Role objects (chatter role, admin role)
 * to avoid fetching them each time from the database
 **/
public interface IActiveRolesService {

    /**
     * Get the chatter role from database or from cache
     **/
    UserRole getChatterRole();

    /**
     * Get the admin role from database or from cache
     **/
    UserRole getAdminRole();
}
