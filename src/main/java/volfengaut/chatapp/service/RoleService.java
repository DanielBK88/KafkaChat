package volfengaut.chatapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.IRoleRepository;
import volfengaut.chatapp.api.service.IRoleService;
import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository repository;
    
    @Override
    public UserRole getRoleByName(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The name should not be null or empty!");
        }
        return repository.getRoleByName(name);
    }

    @Override
    public boolean deleteUserRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("The user role should not be null!");
        } else if (role.getName() == null || role.getName().length() == 0) {
            throw new IllegalArgumentException("The user role's name should not be null or empty!");
        }
        return repository.deleteUserRole(role);
    }

    @Override
    public UserRole addUserRole(String name, Permisson... permissons) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The name should not be null or empty!");
        } else if (permissons.length == 0) {
            throw new IllegalArgumentException("The role should have at least one permission!");
        }
        return repository.addUserRole(name, permissons);
    }

}
