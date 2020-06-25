package volfengaut.chatapp.server.controller;

import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.client_to_client.ChatMessage;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;
import volfengaut.chatapp.message.client_to_server.ClientToServerMessage;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.entity.user.User;

/**
 * Main controller to process incoming messages from clients
 **/
@Controller
public class MessageProcessingMainController {
    
    @Autowired
    private ChatMessageProcessingController chatMessageProcessingController;

    @Autowired
    private ClientToServerMessageProcessingController clientToServerMessageProcessingController;
    
    @Autowired
    private BanMessageProcessingController banMessageProcessingController;
    
    @Autowired
    private IActiveUsersService activeUsersService;
    
    public void processMessage(Socket clientSocket, AbstractMessage message) {

        // Set author and room from the context
        User user = activeUsersService.getActiveUserBySocket(clientSocket);
        message.setAuthorName(user == null ? null : user.getLoginName());
        message.setChatRoomName((user == null || user.getCurrentRoom() == null) ? null : user.getCurrentRoom().getName());
        
        System.out.println("Processing message: " + message);
        
        // Process the message by a specialized processing controller
        if (message instanceof ChatMessage) {
            chatMessageProcessingController.processChatMessage(clientSocket, (ChatMessage) message);
        } else if (message instanceof ChatterBannedMessage) {
            banMessageProcessingController.processBan(clientSocket, (ChatterBannedMessage) message);
        } else if (message instanceof ClientToServerMessage) {
            clientToServerMessageProcessingController.processMessage(clientSocket, (ClientToServerMessage) message);
        }
    }

}
