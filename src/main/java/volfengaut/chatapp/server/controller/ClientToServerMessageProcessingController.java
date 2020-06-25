package volfengaut.chatapp.server.controller;

import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.client_to_server.ClientToServerMessage;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.entity.user.User;

/**
 * A controller to process messages, sent by clients to the server and not to other chatters
 * (such as 'entered login name', 'entered password' etc.)
 **/
@Controller
public class ClientToServerMessageProcessingController {
    
    @Autowired
    private LoginProcessingController loginProcessingController;
    
    @Autowired
    private SignUpProcessingController signUpProcessingController;
    
    @Autowired
    private RoomSelectionProcessingController roomSelectionProcessingController;
    
    @Autowired
    private RoomLeavingProcessingController roomLeavingProcessingController;

    public void processMessage(Socket senderSocket, ClientToServerMessage message) {
        
        switch (message.getMessageType()) {
            
            // Login
            
            case LOGIN_NAME:
                loginProcessingController.processLoginNameProvided(senderSocket, message.getData());
                break;
            case PASSWORD:
                loginProcessingController.processPasswordProvided(
                        senderSocket, message.getAuthorName(), message.getData());
                break;
                
            // Sign-up
                
            case SIGN_UP_LOGIN_NAME:
                signUpProcessingController.processSignUpNameProvided(senderSocket, message.getData());
                break;
            case SIGN_UP_PASSWORD_FIRST:
                signUpProcessingController.processPasswordProvided(
                        senderSocket, message.getAuthorName(), message.getData());
                break;
            case SIGN_UP_PASSWORD_SECOND:
                signUpProcessingController.processPasswordConfirmationProvided(
                        senderSocket, message.getAuthorName(), message.getData());
                break;
                
            // Room selection
            
            case SELECTING_ROOM:
                roomSelectionProcessingController.processRoomEntering(
                        senderSocket, message.getAuthorName(), message.getData());
                break;
            case OPENING_ROOM:
                roomSelectionProcessingController.processRoomOpening(
                        senderSocket, message.getAuthorName(), message.getData());
                break;
                
            // Leaving room
            
            case I_AM_LEAVING_THE_ROOM:
                roomLeavingProcessingController.processLeavingRoom(senderSocket, message.getAuthorName());
                break;
        }
    }
}
