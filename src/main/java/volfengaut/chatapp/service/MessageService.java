package volfengaut.chatapp.service;

import java.util.Collection;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.IMessageRepository;
import volfengaut.chatapp.api.service.IMessageService;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.message.MessageType;
import volfengaut.chatapp.entity.user.User;

@Service
public class MessageService extends AbstractDataService implements IMessageService {

    @Setter(onMethod=@__({@Autowired}))
    private IMessageRepository repository;
    
    @Override
    public void addMessage(Message message) {
        checkMessage(message);
        doInTransaction(m -> {
            repository.addMessage(m);
        }, message, repository);
    }

    @Override
    public Collection<Message> findMessagesSendBy(User user) {
        checkUser(user);
        return doInTransaction(u -> {
            return repository.findMessagesSendBy(u);
        }, user, repository);
    }

    @Override
    public Collection<Message> findMessagesOfType(MessageType type) {
        checkMessageType(type);
        return doInTransaction(t -> {
            return repository.findMessagesOfType(t);
        }, type, repository);
    }

    @Override
    public int countBansOfUser(User user) {
        checkUser(user);
        return doInTransaction(u -> {
            return repository.countBansOfUser(u);
        }, user, repository);
    }

}
