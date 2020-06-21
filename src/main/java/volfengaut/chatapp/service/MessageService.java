package volfengaut.chatapp.service;

import java.util.Collection;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.IMessageRepository;
import volfengaut.chatapp.api.service.IMessageService;
import volfengaut.chatapp.entity.WelcomeMessage;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
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

    @Override
    public String getWelcomeMessage(String language) {
        checkText(language);
        return doInTransaction(l -> {
            WelcomeMessage message = repository.getWelcomeMessage(l);
            if (message == null || StringUtils.isEmpty(message.getMessage())) {
                return "Hello!";
            }
            return message.getMessage();
        }, language, repository);
    }

    @Override
    public void setWelcomeMessage(WelcomeMessage message) {
        chekWelcomeMessage(message);
        doInTransaction((m) -> {
            repository.setWelcomeMessage(m);
        }, message, repository);
    }

}
