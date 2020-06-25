package volfengaut.chatapp.client.controller.message_processing;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;

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
import static volfengaut.chatapp.constant.CommonConstants.MAX_LENGTH;
import static volfengaut.chatapp.constant.CommonConstants.MIN_LENGTH;

/**
 * Used to process incoming server-to-client messages (not sent by a user).
 **/
@Controller
public class ServerToClientMessageProcessingController {
    
    // Command information texts
    private final String LOGIN_CMD_INFO = "To enter a login name:    " + LOGIN_NAME_CMD_PREFIX + " <login_name>";
    private final String PW_CMD_INFO = "To enter the password:    " + PASSWORD_CMD_PREFIX + " <password>";
    private final String SIGN_UP_NAME_CMD_INFO = "To sign up:    " + SIGN_UP_NAME_CMD_PREFIX + " <login_name>";
    private final String SIGN_UP_PW_CMD_INFO = "To select a password:    " + SIGN_UP_PW_CMD_PREFIX + " <password>";
    private final String SIGN_UP_PW_CONF_CMD_INFO = "To confirm the password:    " 
            + SIGN_UP_PW_CONFIRM_CMD_PREFIX + " <password>";
    private final String SELECT_ROOM_CMD_INFO = "To select a room:    " + SELECT_ROOM_CMD_PREFIX + " <room_name>";
    private final String OPEN_ROOM_CMD_INFO = "To open a room:    " + OPEN_ROOM_CMD_PREFIX + " <room_name>";
    private final String PUBLIC_MSG_CMD_INFO = "To send a public message:    " + PUBLIC_MSG_CMD_PREFIX + " <message>";
    private final String PRIVATE_MSG_CMD_INFO = "To send a private message:    " 
            + PRIVATE_MSG_CMD_PREFIX + " <message> " + PRIVATE_MSG_CMD_RECIPIENT_ATTR + " <recipient_name>";
    private final String BAN_MSG_CMD_INFO = "To ban a chatter (needs according permissions):    "
            + BAN_MSG_CMD_PREFIX + " <name_user_to_ban> " + BAN_MSG_CMD_REASON_ATTR + " <reason>";
    private final String LEAVE_ROOM_CMD_INFO = "To leave the room:    " + LEAVE_ROOM_CMD_PREFIX;
    private final String DISCONNECT_CMD_INFO = "To disconnect and exit:    " + DISCONNECT_CMD_PREFIX;

    public void process(ServerToClientMessage message) {
        switch (message.getMessageType()) {
            case PLEASE_ENTER_NAME:
                System.out.println("Please enter a login name or sign up.");
                listAvailableCommands(LOGIN_CMD_INFO, SIGN_UP_NAME_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case UNKNOWN_NAME:
                System.out.println("This login name is not known to our system. " 
                        + "Please enter a different login name or sign up.");
                listAvailableCommands(LOGIN_CMD_INFO, SIGN_UP_NAME_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case NAME_NOT_AVAILABLE:
                System.out.println("This login name is either already taken or does not match the login name criteria. "
                        + "The length of the login name should be min. " + MIN_LENGTH + " and max. " + MAX_LENGTH + "."
                        + "Please enter a different login name or sign up.");
                listAvailableCommands(LOGIN_CMD_INFO, SIGN_UP_NAME_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case PLEASE_ENTER_PASSWORD:
                System.out.println("Please enter your password.");
                listAvailableCommands(PW_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case INVALID_PASSWORD:
                System.out.println("The password you entered is not correct! Please try again or return to login.");
                listAvailableCommands(PW_CMD_INFO, LOGIN_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case SELECT_NEW_PASSWORD:
                System.out.println("Please enter a password for your new account.");
                listAvailableCommands(SIGN_UP_PW_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case NEW_PASSWORD_DOES_NOT_MATCH_CRITERIA:
                System.out.println("The new password does not match the password criteria. " 
                        + "The password should have between " + MIN_LENGTH + " and " + MAX_LENGTH + " symbols " 
                        + "and contain at least: 1 upper case letter, 1 lower case letter, 1 digit, 1 special symbol.");
                listAvailableCommands(SIGN_UP_PW_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case CONFIRM_NEW_PASSWORD:
                System.out.println("Please confirm the new password.");
                listAvailableCommands(SIGN_UP_PW_CONF_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case PASSWORD_CONFIRMATION_FAILED:
                System.out.println("Password confirmation failed. Please reenter your password once again.");
                listAvailableCommands(SIGN_UP_PW_CONF_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case SELECT_CHAT_ROOM:
                System.out.println("Please select a chat room or open up a new room.");
                System.out.println("Currently open rooms: " + (StringUtils.isEmpty(message.getProvidedData()) 
                        ? "No rooms open." : message.getProvidedData()));
                listAvailableCommands(SELECT_ROOM_CMD_INFO, OPEN_ROOM_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case UNKNOWN_CHAT_ROOM:
                System.out.println("Currently, there is no open room with the specified name.");
                System.out.println("Open rooms: " + (StringUtils.isEmpty(message.getProvidedData())
                        ? "No rooms open." : message.getProvidedData()));
                listAvailableCommands(SELECT_ROOM_CMD_INFO, OPEN_ROOM_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case CHAT_ROOM_OPENING_NOT_ALLOWED:
                System.out.println("Sorry, you have no permission to open a new chat room! " 
                        + "Please select one of the existing rooms!");
                System.out.println("Open rooms: " + (StringUtils.isEmpty(message.getProvidedData())
                        ? "No rooms open." : message.getProvidedData()));
                listAvailableCommands(SELECT_ROOM_CMD_INFO, DISCONNECT_CMD_INFO);
                break;
            case EXISTING_CHATTERS_INFO:   
                System.out.println("You entered the room successfully. Have fun :)");
                System.out.println("Other chatters in this room: " + (StringUtils.isEmpty(message.getProvidedData())
                        ? "No other chatters." : message.getProvidedData()));
                listAvailableCommands(PUBLIC_MSG_CMD_INFO, PRIVATE_MSG_CMD_INFO, BAN_MSG_CMD_INFO, LEAVE_ROOM_CMD_INFO);
                break;
            case CHATTER_ENTERED:
                System.out.println("User entered: " + message.getProvidedData());
                break;
            case CHATTER_LEFT:
                System.out.println("User left: " + message.getProvidedData());
                break;
            case NO_PERMISSION_TO_BAN_USERS:
                System.out.println("Sorry, you have no permission to ban users!");
        }
    }
    
    private void listAvailableCommands(String... commandInfos) {
        System.out.println("Available commands: ");
        Arrays.stream(commandInfos).forEach(System.out::println);
    }
}
