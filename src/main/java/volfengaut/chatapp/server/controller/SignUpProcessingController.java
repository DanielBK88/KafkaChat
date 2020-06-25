package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveRolesService;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessageService;
import volfengaut.chatapp.server.api.service.IMessagingService;
import volfengaut.chatapp.server.api.service.IUserService;
import volfengaut.chatapp.server.entity.user.User;

import static volfengaut.chatapp.constant.CommonConstants.DATA_DELIMITER;
import static volfengaut.chatapp.constant.CommonConstants.MAX_LENGTH;
import static volfengaut.chatapp.constant.CommonConstants.MIN_LENGTH;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.CONFIRM_NEW_PASSWORD;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.NAME_NOT_AVAILABLE;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.NEW_PASSWORD_DOES_NOT_MATCH_CRITERIA;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.PASSWORD_CONFIRMATION_FAILED;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.SELECT_CHAT_ROOM;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.SELECT_NEW_PASSWORD;

/**
 * The controller processing the signing up of a new user
 * 
 * NOTE: Messages to clients, which are send by this class, will in most cases be send with userName = null,
 * because the user is not persisted until the sign up is completed. As soon, as he signs up successfully,
 * all messages to this user are found in db by the client address column and their user field is updated.
 **/
@Controller
public class SignUpProcessingController {
    
    @Autowired
    private IUserService userService;

    @Autowired
    private IActiveUsersService activeUserService;
    
    @Autowired
    private IActiveRolesService activeRolesService;

    @Autowired
    private IActiveRoomsService activeRoomsService;

    @Autowired
    private IMessagingService messagingService;
    
    @Autowired
    private IMessageService messageService;

    /**
     * Process a login name provided during sign-up process
     **/
    public void processSignUpNameProvided(Socket clientSocket, String signUpName) {
        
        String clientAddress = activeUserService.getSocketAddress(clientSocket);
        
        // Check if such a user name does not already exist and if it matches the criteria for a user name
        if (userService.getUserByName(signUpName) != null || !validateUserName(signUpName)) {
            messagingService.send(clientSocket, new ServerToClientMessage(NAME_NOT_AVAILABLE, null, 
                    LocalDateTime.now(), null, clientAddress));
        } else {
            
            // The new name is OK. Tell the user to select a password
            messagingService.send(clientSocket, new ServerToClientMessage(SELECT_NEW_PASSWORD, null, 
                    LocalDateTime.now(), null, clientAddress));
            
            // Register the new user in the context (do not yet save him to the database)
            User newUser = new User();
            newUser.setLoginName(signUpName);
            newUser.setDateJoined(LocalDate.now());
            newUser.setRole(activeRolesService.getChatterRole());
            newUser.setCurrentInetAddress(clientAddress);
            activeUserService.setActiveUser(clientSocket, newUser);
        }
    }
    
    /**
     * Process a new password provided during sign-up process
     **/
    public void processPasswordProvided(Socket clientSocket, String userName, String newPassword) {
        
        // Get the active user from the context
        User user = activeUserService.getActiveUserByName(userName);
        
        // Check, if the new password matches the criteria for a password
        if (!validatePassword(newPassword)) {
            messagingService.send(clientSocket, new ServerToClientMessage(
                    NEW_PASSWORD_DOES_NOT_MATCH_CRITERIA, null, LocalDateTime.now(), null, 
                    activeUserService.getSocketAddress(clientSocket)));
        } else {
            
            // New password is OK. Save it to the context and ask the user to reenter the password for confirmation.
            user.setPassword(newPassword);
            messagingService.send(clientSocket, new ServerToClientMessage(CONFIRM_NEW_PASSWORD, null, 
                    LocalDateTime.now(), null, activeUserService.getSocketAddress(clientSocket)));
        }
    }
    
    public void processPasswordConfirmationProvided(Socket clientSocket, String userName, String passwordConfirmation) {

        // Get the active user from the context
        User user = activeUserService.getActiveUserByName(userName);
        
        // Check if the new password matches the already registered one
        if (!user.getPassword().matches(passwordConfirmation)) {
            messagingService.send(clientSocket, new ServerToClientMessage(
                    PASSWORD_CONFIRMATION_FAILED, null, LocalDateTime.now(), null, 
                    activeUserService.getSocketAddress(clientSocket)));
        } else {
            
            // Password confirmation successful. Now save the new user to the DB
            userService.addUser(user);
            
            // Now we need to update the user field on messages to this user, which were send earlier with user = null.
            messageService.updateServerMessagesForUser(user);
            
            // Finally, tell the user to select a room and inform him about existing rooms.
            String existingRoomNames = String.join(DATA_DELIMITER, activeRoomsService.getNamesOfActiveRooms());
            messagingService.send(clientSocket, new ServerToClientMessage(SELECT_CHAT_ROOM, userName, 
                    LocalDateTime.now(), existingRoomNames, activeUserService.getSocketAddress(clientSocket)));
            
        }
    }
    
    /**
     * Check that the provided user names matches the user name criteria
     **/
    private boolean validateUserName(String userName) {
        return MIN_LENGTH <= userName.length() && MAX_LENGTH >= userName.length(); 
    }

    /**
     * Check that the provided password matches the password criteria
     **/
    private boolean validatePassword(String password) {
        if(MIN_LENGTH > password.length() || MAX_LENGTH < password.length()) {
            return false;
        }
        boolean lowerCaseFound = false;
        boolean upperCaseFound = false;
        boolean digitFound = false;
        boolean specialCharFound = false;
        for (Character c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCaseFound = true;
            } else if (Character.isLowerCase(c)) {
                lowerCaseFound = true;
            } else if (Character.isDigit(c)) {
                digitFound = true;
            } else if (c.equals(' ')) {
                return false; // Whitespaces not allowed in the password!
            } else {
                specialCharFound = true;
            }
        }
        return lowerCaseFound && upperCaseFound && digitFound && specialCharFound;
    }
}
