package volfengaut.chatapp.controller;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.service.ChatRoomService;

import static volfengaut.chatapp.entity.role.Permisson.CREATE_CHAT_ROOM;

/**
 * The controller handling the selection of a chat room by a chatter
 **/
@Controller
public class SelectRoomController {

    /**
     * Command to exit the system
     **/
    private static final String EXIT_COMMAND = "exit";

    /**
     * Command to submit at any question
     **/
    private static final String SUBMIT_COMMAND = "yes";

    /**
     * Command to decline at any question
     **/
    private static final String DECLINE_COMMAND = "no";

    /**
     * Command to select another room (after the desired room was not found)
     **/
    private static final String OTHER_ROOM_COMMAND = "other";

    @Autowired
    private Scanner scanner;
    
    @Autowired
    private ChatRoomService chatRoomService;
    
    public ChatRoom processRoomSelection(User user) {
        roomSelection : while (true) {
            System.out.println("Please choose a chat room name:");
            String roomName = scanner.nextLine();
            ChatRoom room = chatRoomService.getChatRoom(roomName);
            if (room != null) {
                return room;
            } else if (user.hasPermission(CREATE_CHAT_ROOM)) {
                System.out.println("The specified room does not yet exist. Do you want to create it?");
                while (true) {
                    System.out.println("Type " + SUBMIT_COMMAND + " to create a new room with this name.");
                    System.out.println("Type " + DECLINE_COMMAND + " to enter a new room name.");
                    String command = scanner.nextLine();
                    switch (command) {
                        case SUBMIT_COMMAND:
                            return chatRoomService.createChatRoom(roomName, user);
                        case DECLINE_COMMAND:
                            continue roomSelection;
                        default:
                            System.out.println("Oops, this was not a valid command!");
                    }
                }
            } else {
                System.out.println("The specified room does not exist");
                System.out.println("Unfortunately, you do not have the permission to create a new room!");
                while (true) {
                    System.out.println("Type " + OTHER_ROOM_COMMAND + " to select a different room.");
                    System.out.println("Type " + EXIT_COMMAND + " to quit the system.");
                    String command = scanner.nextLine();
                    switch (command) {
                        case OTHER_ROOM_COMMAND:
                            continue roomSelection;
                        case EXIT_COMMAND:
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Oops, this was not a valid command!");
                    }
                }
            }
        }

    }

}
