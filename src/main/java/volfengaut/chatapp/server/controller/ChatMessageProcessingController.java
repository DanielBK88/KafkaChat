package volfengaut.chatapp.server.controller;

import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.client_to_client.ChatMessage;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessagingService;
import volfengaut.chatapp.server.entity.user.User;

/**
 * Controller to process incoming chat messages and to resend them to their recipients.
 **/
@Controller
public class ChatMessageProcessingController {
    
    @Autowired
    private IMessagingService messagingService;
    
    @Autowired
    private IActiveUsersService activeUserService;

    public void processChatMessage(Socket senderSocket, ChatMessage message) {
        
        String recipientName = message.getRecipientName();
        if (recipientName == null) {
            
            // It is a public message ==> resend to everyone except the sender within in the sender's room
            User user = activeUserService.getActiveUserByName(message.getAuthorName());
            
            for (String chatterName : activeUserService.getNamesOfChattersInRoom(user.getCurrentRoom().getName())) {
                if (chatterName.equals(message.getAuthorName())) {
                    continue;
                }
                Socket socket = activeUserService.getActiveUserSocket(chatterName);
                messagingService.send(socket, message);
            }
        } else {
            
            // It is a private message ==> resend only to recipient
            Socket recipientSocket = activeUserService.getActiveUserSocket(recipientName);
            messagingService.send(recipientSocket, message);
        }
    }

}
