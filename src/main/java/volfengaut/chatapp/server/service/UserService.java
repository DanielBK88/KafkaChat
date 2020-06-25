package volfengaut.chatapp.server.service;

import java.time.LocalDate;
import java.util.Collection;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.server.api.repository.IUserRepository;
import volfengaut.chatapp.server.api.service.IUserService;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.role.UserRole;
import volfengaut.chatapp.server.entity.user.User;

@Service
public class UserService extends AbstractDataService implements IUserService {

    @Setter(onMethod=@__({@Autowired}))
    private IUserRepository repository;

    
    @Override
    public User getUserByName(String name) {
        checkUserName(name);
        return doInTransaction(n -> {
            return repository.getUserByName(n);
        }, name, repository);
    }

    @Override
    public void deleteUser(User user) {
        checkUser(user);
        doInTransaction(u -> {
            repository.deleteUser(u);
        }, user, repository);
    }

    @Override
    public void addUser(User user) {
        checkUser(user);
        doInTransaction(u -> {
            repository.addUser(u);
        }, user, repository);
    }

    @Override
    public Collection<User> getUsersByRole(UserRole role) {
        checkRole(role);
        return doInTransaction(r -> {
            return repository.getUsersByRole(r);
        }, role, repository);
    }

    @Override
    public Collection<User> getAuthorsOfMessagesContaining(String text) {
        checkText(text);
        return doInTransaction(t -> {
            return repository.getAuthorsOfMessagesContaining(t);
        }, text, repository);
    }

    @Override
    public Collection<User> getUsersJoinedAfter(LocalDate time) {
        checkDate(time);
        return doInTransaction(t -> {
            return repository.getUsersJoinedAfter(t);
        }, time, repository);
    }

    @Override
    public Collection<User> getChattersBannedFrom(ChatRoom room) {
        checkRoom(room);
        return doInTransaction(r -> {
            return repository.getChattersBannedFrom(r);
        }, room, repository);
    }

    @Override
    public Collection<User> getChattersBannedBy(User user) {
        checkUser(user);
        return doInTransaction(u -> {
            return repository.getChattersBannedBy(u);
        }, user, repository); 
    }

}
