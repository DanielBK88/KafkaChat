package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IChatRoomService;
import volfengaut.chatapp.server.api.service.IMessagingService;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.role.Permission;
import volfengaut.chatapp.server.entity.user.User;
import java.util.List;

import static volfengaut.chatapp.constant.CommonConstants.DATA_DELIMITER;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.CHATTER_ENTERED;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.CHAT_ROOM_OPENING_NOT_ALLOWED;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.EXISTING_CHATTERS_INFO;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.UNKNOWN_CHAT_ROOM;

/**
 * A controller used to handle the process of room selection by a client
 **/
@Controller
public class RoomSelectionProcessingController {
    
    @Autowired
    private IActiveRoomsService activeRoomsService;
    
    @Autowired
    private IActiveUsersService activeUsersService;
    
    @Autowired
    private IChatRoomService chatRoomService;
    
    @Autowired
    private IMessagingService messagingService;

    /**
     * Handles the selection of a room by the user
     **/
    public void processRoomEntering(Socket clientSocket, String userName, String roomNameProvided) {
        
        String clientAddress = activeUsersService.getSocketAddress(clientSocket);
        
        //Check if the specified room is currently active:
        ChatRoom room = activeRoomsService.getActiveRoom(roomNameProvided);
        if (room == null) {
            
            // The room is currently not active. Tell this to the client. He can create it if he wants to.
            messagingService.send(clientSocket, new ServerToClientMessage(
                    UNKNOWN_CHAT_ROOM, userName, LocalDateTime.now(), null, clientAddress));
        } else {

            // The room is active. Inform the user about other chatters in this room
            List<String> otherChatterNames = activeUsersService.getNamesOfChattersInRoom(room.getName());
            messagingService.send(clientSocket, new ServerToClientMessage(EXISTING_CHATTERS_INFO, userName,
                    LocalDateTime.now(), String.join(DATA_DELIMITER, otherChatterNames), clientAddress));

            // Inform the other chatters in this room about the new entering chatter
            for (String otherChatterName : otherChatterNames) {
                Socket otherChatterSocket = activeUsersService.getActiveUserSocket(otherChatterName);
                messagingService.send(otherChatterSocket, new ServerToClientMessage(CHATTER_ENTERED, otherChatterName,
                        LocalDateTime.now(), userName, activeUsersService.getSocketAddress(otherChatterSocket)));
            }
            
            // Finally, add the chatter to this room.
            activeUsersService.addChatterToRoom(room, userName);
        }
    }
    
    /**
     * Handles the opening of a new room by the user
     **/
    public void processRoomOpening(Socket clientSocket, String userName, String newRoomName) {

        String clientAddress = activeUsersService.getSocketAddress(clientSocket);
        
        // Check if the user has the permission to open new rooms
        User user = activeUsersService.getActiveUserByName(userName);
        if (!user.hasPermission(Permission.OPEN_CHAT_ROOM)) {
            messagingService.send(clientSocket, new ServerToClientMessage(
                    CHAT_ROOM_OPENING_NOT_ALLOWED, userName, LocalDateTime.now(), null, clientAddress));
        } else {

            // Check if a room with this name already exists in the database and create it if needed
            ChatRoom room = chatRoomService.getChatRoom(newRoomName);
            if (room == null) {
                room = new ChatRoom(newRoomName, user);
                chatRoomService.createChatRoom(room);
            }
            
            // Activate the room and add the chatter to it. Inform him about the list of current chatters (empty list)
            activeRoomsService.addActiveRoom(room);
            activeUsersService.addChatterToRoom(room, userName);
            messagingService.send(clientSocket, new ServerToClientMessage(EXISTING_CHATTERS_INFO, userName,
                    LocalDateTime.now(), "", clientAddress));
        }
        
    }
}
