package volfengaut.chatapp.api.repository;

import javax.persistence.EntityManager;

/**
 * Implemented by repositories.
 * Allows providing them the actual entity manager to fulfill database operations
 **/
public interface EntityManagerDependent {
    
    /**
     * Pass the actual entity manager to the repository
     **/
    void setEntityManager(EntityManager em);

}
