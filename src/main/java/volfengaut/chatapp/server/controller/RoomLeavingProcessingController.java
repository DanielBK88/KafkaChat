package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessagingService;

import static volfengaut.chatapp.constant.CommonConstants.DATA_DELIMITER;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.CHATTER_LEFT;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.SELECT_CHAT_ROOM;

/**
 * Handles the leaving of a room
 **/
@Controller
public class RoomLeavingProcessingController {
    
    @Autowired
    private IActiveUsersService activeUsersService;

    @Autowired
    private IActiveRoomsService activeRoomsService;

    @Autowired
    private IMessagingService messagingService;

    /**
     * Handle the leaving of a room
     **/
    public void processLeavingRoom(Socket clientSocket, String userName) {
        
        String clientAddress = activeUsersService.getSocketAddress(clientSocket);
        
        // Get the name of the chatter's current room from the context
        String currentRoomName = activeUsersService.getActiveUserByName(userName).getCurrentRoom().getName();

        // Inform other chatters in this room that the chatter is leaving
        List<String> otherChatterNames = activeUsersService.getNamesOfChattersInRoom(currentRoomName);
        for (String otherChatterName : otherChatterNames) {
            Socket otherChatterSocket = activeUsersService.getActiveUserSocket(otherChatterName);
            if (otherChatterName.equals(userName)) {
                continue;
            }
            messagingService.send(otherChatterSocket, new ServerToClientMessage(CHATTER_LEFT,
                    otherChatterName, LocalDateTime.now(), null, clientAddress));
        }
        
        // Update the user in the the context
        activeUsersService.removeChatterFromHisRoom(userName);
        
        // Ask him to enter a new room name and inform him about currently open rooms
        String existingRoomNames = String.join(DATA_DELIMITER, activeRoomsService.getNamesOfActiveRooms());
        messagingService.send(clientSocket, new ServerToClientMessage(
                SELECT_CHAT_ROOM, userName, LocalDateTime.now(), existingRoomNames, clientAddress));
    }
}
