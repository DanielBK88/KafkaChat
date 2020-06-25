package volfengaut.chatapp.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessageService;
import volfengaut.chatapp.server.api.service.IMessagingService;

@Service
public class MessagingService implements IMessagingService {

    @Autowired
    private IMessageService messageService;
    
    @Autowired
    private IActiveUsersService activeUsersService;
    
    @Autowired
    private IActiveRoomsService activeRoomsService;
    
    @Override
    public void send(Socket destination, AbstractMessage message) {
        try {
            ObjectOutputStream outputStream = activeUsersService.getObjectOutputStreamForSocket(destination);
            outputStream.writeObject(message);
            messageService.addMessage(message.toPersistableMessage(
                    activeUsersService.getActiveUserByName(message.getAuthorName()),
                    activeUsersService.getActiveUserByName(message.getRecipientName()),
                    activeRoomsService.getActiveRoom(message.getChatRoomName())
            ));
            System.out.println("Sending message: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractMessage receive(Socket source) {
        try {
            ObjectInputStream inputStream = activeUsersService.getObjectInputStreamForSocket(source);
            AbstractMessage message = (AbstractMessage) inputStream.readObject();
            messageService.addMessage(message.toPersistableMessage(
                    activeUsersService.getActiveUserByName(message.getAuthorName()),
                    activeUsersService.getActiveUserByName(message.getRecipientName()),
                    activeRoomsService.getActiveRoom(message.getChatRoomName())
            ));
            System.out.println("Reveiving message: " + message);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
