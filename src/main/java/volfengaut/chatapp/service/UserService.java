package volfengaut.chatapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.IUserRepository;
import volfengaut.chatapp.api.service.IUserService;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;
    
    @Override
    public User getUserByName(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The name should not be null or empty!");
        }
        return repository.getUserByName(name);
    }

    @Override
    public boolean deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User should not be null!");
        } else if (user.getLoginName() == null) {
            throw new IllegalArgumentException("The user's login name should not be null!");
        }
        return repository.deleteUser(user);
    }

    @Override
    public User addUser(String name, UserRole role) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The name should not be null or empty!");
        } else if (role == null) {
            throw new IllegalArgumentException("The user role should not be null!");
        }
        return repository.addUser(name, role);
    }

}
