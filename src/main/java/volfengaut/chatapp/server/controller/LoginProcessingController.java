package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessagingService;
import volfengaut.chatapp.server.api.service.IUserService;
import volfengaut.chatapp.server.entity.user.User;

import static volfengaut.chatapp.constant.CommonConstants.DATA_DELIMITER;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.INVALID_PASSWORD;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.PLEASE_ENTER_PASSWORD;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.SELECT_CHAT_ROOM;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.UNKNOWN_NAME;

/**
 * The controller handling the login process
 **/
@Controller
public class LoginProcessingController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IActiveUsersService activeUserService;
    
    @Autowired
    private IActiveRoomsService activeRoomsService;

    @Autowired
    private IMessagingService messagingService;

    /**
     * Handle a client message providing a login name
     **/
    public void processLoginNameProvided(Socket senderSocket, String loginName) {
        
        String clientAddress = activeUserService.getSocketAddress(senderSocket);
        
        // Look for this user in the database.
        User user = userService.getUserByName(loginName);
        if (user == null) {

            // Unknown user. Tell him to either sign up or retry login
            messagingService.send(senderSocket, new ServerToClientMessage(
                    UNKNOWN_NAME, null, LocalDateTime.now(), null, clientAddress));
        } else {

            // Known user. Register him and tell him to enter his password.
            activeUserService.setActiveUser(senderSocket, user);

            messagingService.send(senderSocket, new ServerToClientMessage(
                    PLEASE_ENTER_PASSWORD, null, LocalDateTime.now(), null, clientAddress));
        }
    }

    /**
     * Handle a client message providing the user password
     **/
    public void processPasswordProvided(Socket senderSocket, String loginName, String providedPassword) {

        String clientAddress = activeUserService.getSocketAddress(senderSocket);
        
        // Get the active user from the context and compare his actual password with the provided one.
        User user = activeUserService.getActiveUserByName(loginName);
        String actualPassword = user.getPassword();
        
        if (actualPassword.equals(providedPassword)) {
            
            // Passwords match. Tell him to select a room and inform him about existing rooms.
            String existingRoomNames = String.join(DATA_DELIMITER, activeRoomsService.getNamesOfActiveRooms());
            messagingService.send(senderSocket, new ServerToClientMessage(
                    SELECT_CHAT_ROOM, null, LocalDateTime.now(), existingRoomNames, clientAddress));
        } else {
            
            // Passwords do not match. Tell him to retry entering his password or to select a different login name.
            messagingService.send(senderSocket, new ServerToClientMessage(
                    INVALID_PASSWORD, loginName, LocalDateTime.now(), null, clientAddress));
        }
    }
}
