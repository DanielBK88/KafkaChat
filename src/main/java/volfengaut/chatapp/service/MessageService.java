package volfengaut.chatapp.service;

import java.util.Collection;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.IMessageRepository;
import volfengaut.chatapp.api.service.IMessageService;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
import volfengaut.chatapp.entity.message.MessageType;
import volfengaut.chatapp.entity.user.User;

@Service
public class MessageService extends AbstractDataService implements IMessageService {

    @Setter(onMethod=@__({@Autowired}))
    private IMessageRepository repository;
    
    @Override
    public void addMessage(AbstractMessageEntity message) {
        checkMessage(message);
        doInTransaction(m -> {
            repository.addMessage(m);
        }, message, repository);
    }

    @Override
    public Collection<ChatMessageEntity> findMessagesSendBy(User user) {
        checkUser(user);
        return doInTransaction(u -> {
            return repository.findMessagesSendBy(u);
        }, user, repository);
    }

    @Override
    public <T extends AbstractMessageEntity> Collection<T> findMessagesOfType(Class<T> messageClass) {
        return doInTransaction(m -> {
            return repository.findMessagesOfType(m);
        }, messageClass, repository);
    }

    @Override
    public int countBansOfUser(User user) {
        checkUser(user);
        return doInTransaction(u -> {
            return repository.countBansOfUser(u);
        }, user, repository);
    }

}
