package volfengaut.chatapp.repository;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IRoleRepository;
import volfengaut.chatapp.entity.role.UserRole;

@Repository
public class RoleRepository implements IRoleRepository {
    
    private EntityManager entityManager;
    
    @Override
    public UserRole getRoleByName(String name) {
        return entityManager.find(UserRole.class, name);
    }

    @Override
    public void deleteUserRole(UserRole role) {
        entityManager.remove(role);
    }

    @Override
    public void addUserRole(UserRole role) {
        entityManager.persist(role);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
