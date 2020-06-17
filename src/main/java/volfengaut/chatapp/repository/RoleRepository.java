package volfengaut.chatapp.repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IRoleRepository;
import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;

@Repository
public class RoleRepository implements IRoleRepository {
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Override
    public UserRole getRoleByName(String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        UserRole role = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            role = em.find(UserRole.class, name);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return role;
    }

    @Override
    public boolean deleteUserRole(UserRole role) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            em.remove(role);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
        return true;
    }

    @Override
    public UserRole addUserRole(String name, Permisson... permissons) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        UserRole role = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            role = new UserRole(name, Arrays.stream(permissons).collect(Collectors.toSet()), new HashSet<>());
            em.persist(role);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return role;
    }

}
