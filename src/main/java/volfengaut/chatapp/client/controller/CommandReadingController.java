package volfengaut.chatapp.client.controller;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.client.api.service.IMessagingService;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.client_to_client.ChatMessage;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;
import volfengaut.chatapp.message.client_to_server.ClientToServerMessage;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static volfengaut.chatapp.client.constant.CommandPrefixes.BAN_MSG_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.BAN_MSG_CMD_REASON_ATTR;
import static volfengaut.chatapp.client.constant.CommandPrefixes.DISCONNECT_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.LEAVE_ROOM_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.LOGIN_NAME_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.OPEN_ROOM_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.PASSWORD_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.PRIVATE_MSG_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.PRIVATE_MSG_CMD_RECIPIENT_ATTR;
import static volfengaut.chatapp.client.constant.CommandPrefixes.PUBLIC_MSG_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.SELECT_ROOM_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.SIGN_UP_NAME_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.SIGN_UP_PW_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.SIGN_UP_PW_CONFIRM_CMD_PREFIX;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.I_AM_DISCONNECTING;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.I_AM_LEAVING_THE_ROOM;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.LOGIN_NAME;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.OPENING_ROOM;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.PASSWORD;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.SELECTING_ROOM;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.SIGN_UP_LOGIN_NAME;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.SIGN_UP_PASSWORD_FIRST;
import static volfengaut.chatapp.message.client_to_server.ClientToServerMessageType.SIGN_UP_PASSWORD_SECOND;

@Controller
public class CommandReadingController {

    @Autowired
    private Scanner scanner;
    
    @Autowired
    private IMessagingService messagingService;
    
    public void process() {
        System.out.println("Welcome to the chat application! :)");
        boolean exitFlag = false;
        while(!exitFlag) {
            String command = scanner.nextLine();
            AbstractMessage message = null;
            if (command.startsWith(LOGIN_NAME_CMD_PREFIX + SPACE)) {
                String name = command.substring(LOGIN_NAME_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(LOGIN_NAME, name);
            } else if (command.startsWith(PASSWORD_CMD_PREFIX + SPACE)) {
                String pw = command.substring(PASSWORD_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(PASSWORD, pw);
            } else if (command.startsWith(SIGN_UP_NAME_CMD_PREFIX + SPACE)) {
                String name = command.substring(SIGN_UP_NAME_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(SIGN_UP_LOGIN_NAME, name);
            } else if (command.startsWith(SIGN_UP_PW_CMD_PREFIX + SPACE)) {
                String pw = command.substring(SIGN_UP_PW_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(SIGN_UP_PASSWORD_FIRST, pw);
            } else if (command.startsWith(SIGN_UP_PW_CONFIRM_CMD_PREFIX + SPACE)) {
                String pw2 = command.substring(SIGN_UP_PW_CONFIRM_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(SIGN_UP_PASSWORD_SECOND, pw2);
            } else if (command.startsWith(SELECT_ROOM_CMD_PREFIX + SPACE)) {
                String room = command.substring(SELECT_ROOM_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(SELECTING_ROOM, room);
            } else if (command.startsWith(OPEN_ROOM_CMD_PREFIX + SPACE)) {
                String room = command.substring(OPEN_ROOM_CMD_PREFIX.length() + 1);
                message = new ClientToServerMessage(OPENING_ROOM, room);
            } else if (command.startsWith(PUBLIC_MSG_CMD_PREFIX + SPACE)) {
                String text = command.substring(PUBLIC_MSG_CMD_PREFIX.length() + 1);
                message = new ChatMessage(text, null);
                System.out.println("You (to all): " + message);
            } else if (command.startsWith(PRIVATE_MSG_CMD_PREFIX + SPACE)) {
                String text = command.substring(PRIVATE_MSG_CMD_PREFIX.length() + 1);
                if (!text.contains(PRIVATE_MSG_CMD_RECIPIENT_ATTR)) {
                    System.out.println("Please specify a recipient of the private message " 
                            + "by using the attribute " + PRIVATE_MSG_CMD_RECIPIENT_ATTR + " in the end!");
                    continue;
                }
                String[] parts = text.split(PRIVATE_MSG_CMD_RECIPIENT_ATTR + SPACE);
                String msgText = parts[0];
                String recipient = parts[1];
                System.out.println("You (to " + recipient + "): " + msgText);
                message = new ChatMessage(msgText, recipient);
            } else if (command.startsWith(BAN_MSG_CMD_PREFIX + SPACE)) {
                String text = command.substring(BAN_MSG_CMD_PREFIX.length() + 1);
                if (!text.contains(BAN_MSG_CMD_REASON_ATTR + SPACE)) {
                    System.out.println("Please specify a reason of the ban "
                            + "by using the attribute " + BAN_MSG_CMD_REASON_ATTR + " in the end!");
                    continue;
                }
                String[] parts = text.split(BAN_MSG_CMD_REASON_ATTR + SPACE);
                String bannedUser = parts[0];
                String reason = parts[1];
                System.out.println("You banned " + bannedUser + ".");
                message = new ChatterBannedMessage(reason, bannedUser);
            } else if (command.startsWith(LEAVE_ROOM_CMD_PREFIX)) {
                message = new ClientToServerMessage(I_AM_LEAVING_THE_ROOM, null);
            } else if (command.startsWith(DISCONNECT_CMD_PREFIX)) {
                message = new ClientToServerMessage(I_AM_DISCONNECTING, null);
                exitFlag = true;
            } else {
                System.out.println("Invalid message!");
                continue;
            }
            messagingService.send(message);
        }
    }
}
