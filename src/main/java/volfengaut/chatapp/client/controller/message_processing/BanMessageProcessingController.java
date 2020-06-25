package volfengaut.chatapp.client.controller.message_processing;

import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;

import static volfengaut.chatapp.client.constant.CommandPrefixes.DISCONNECT_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.OPEN_ROOM_CMD_PREFIX;
import static volfengaut.chatapp.client.constant.CommandPrefixes.SELECT_ROOM_CMD_PREFIX;

/**
 * Used to process incoming ban messages
 **/
@Controller
public class BanMessageProcessingController {

    public void process(ChatterBannedMessage message) {
        String author = message.getAuthorName();
        String reason = message.getBanReason();
        String room = message.getChatRoomName();
        System.out.println(author + " has banned you from the room " + room + "! Reason: " + reason);
        System.out.println("Possible commands: ");
        System.out.println(SELECT_ROOM_CMD_PREFIX + " <some_room_name>     ->     select a room");
        System.out.println(OPEN_ROOM_CMD_PREFIX + " <some_room_name>     ->     open a new room");
        System.out.println(DISCONNECT_CMD_PREFIX + "     ->     disconnect");
    }
}
