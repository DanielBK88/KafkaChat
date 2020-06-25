package volfengaut.chatapp.client.service;

import java.io.ObjectOutputStream;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.client.api.service.IMessagingService;
import volfengaut.chatapp.message.AbstractMessage;

@Service
public class MessagingService implements IMessagingService {

    @Autowired
    private ObjectOutputStream serverOutputStream;
    
    @Override
    public void send(AbstractMessage message) {
        try {
            serverOutputStream.writeObject(message);
            System.out.println("Sent message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
