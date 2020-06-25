package volfengaut.chatapp.client.controller;

import java.io.ObjectInputStream;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.client.controller.message_processing.BanMessageProcessingController;
import volfengaut.chatapp.client.controller.message_processing.ChatMessageProcessingController;
import volfengaut.chatapp.client.controller.message_processing.ServerToClientMessageProcessingController;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.client_to_client.ChatMessage;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;

/**
 * Used to listen for messages from server
 **/
@Controller
public class ServerListeningController {
    
    @Autowired
    private ChatMessageProcessingController chatMessageProcessingController;
    
    @Autowired
    private BanMessageProcessingController banMessageProcessingController;
    
    @Autowired
    private ServerToClientMessageProcessingController serverToClientMessageProcessingController;

    public void listen(Socket server) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());

            while (true) {
                
                // Wait for a new message
                AbstractMessage message = (AbstractMessage) inputStream.readObject();
                
                // Process the received message by a specialized controller.
                if (message instanceof ChatMessage) {
                    chatMessageProcessingController.process((ChatMessage) message);
                } else if (message instanceof ChatterBannedMessage) {
                    banMessageProcessingController.process((ChatterBannedMessage) message);
                } else if (message instanceof ServerToClientMessage) {
                    serverToClientMessageProcessingController.process((ServerToClientMessage) message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
