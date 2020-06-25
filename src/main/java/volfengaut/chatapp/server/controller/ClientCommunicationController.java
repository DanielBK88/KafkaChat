package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.client_to_server.ClientToServerMessage;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessagingService;

import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.I_AM_DISCONNECTING;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.PLEASE_ENTER_NAME;

/**
 * Controller handling the communication with clients.
 * Waits for client messages and submits tasks to the executor service to process those messages,
 * while the controller itself keeps on listening for new messages.
 **/
@Controller
public class ClientCommunicationController {
    
    @Autowired
    private IMessagingService messagingService;
    
    @Autowired
    private ExecutorService executorService;
    
    @Autowired
    private MessageProcessingMainController messageProcessingMainController;
    
    @Autowired
    private IActiveUsersService activeUsersService;
    
    public void communicate(Socket clientSocket) {
        
        // Send a request to enter a login name
        messagingService.send(clientSocket, new ServerToClientMessage(PLEASE_ENTER_NAME, null, 
                LocalDateTime.now(), null, activeUsersService.getSocketAddress(clientSocket)));
        
        while (true) {
            
            // Wait for a new client message
            AbstractMessage message = messagingService.receive(clientSocket);

            // Exit the loop if the client is telling us, that he is disconnecting
            if (message instanceof ClientToServerMessage 
                    && ((ClientToServerMessage) message).getMessageType().equals(I_AM_DISCONNECTING)) {
                break;
            }
            
            // Submit an asynchronous task to process the received message
            messageProcessingMainController.processMessage(clientSocket, message);
        }
    }

}
