package volfengaut.chatapp.server.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.server.api.repository.IRoleRepository;
import volfengaut.chatapp.server.api.service.IRoleService;
import volfengaut.chatapp.server.entity.role.UserRole;

@Service
public class RoleService extends AbstractDataService implements IRoleService {

    @Setter(onMethod=@__({@Autowired}))
    private IRoleRepository repository;
    
    @Override
    public UserRole getRoleByName(String name) {
        checkRoleName(name);
        return doInTransaction(n -> {
            return repository.getRoleByName(name);
        }, name, repository);
    }

    @Override
    public void deleteUserRole(UserRole role) {
        checkRole(role);
        doInTransaction(r -> {
            repository.deleteUserRole(r);
        }, role, repository);
    }

    @Override
    public void addUserRole(UserRole role) {
        checkRole(role);
        doInTransaction(r -> {
            repository.addUserRole(r);
        }, role, repository);
    }

}
