package volfengaut.chatapp.server.controller;

import java.net.Socket;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.api.service.IActiveUsersService;
import volfengaut.chatapp.server.api.service.IMessagingService;
import volfengaut.chatapp.server.entity.role.Permission;
import volfengaut.chatapp.server.entity.user.User;
import java.util.List;

import static volfengaut.chatapp.constant.CommonConstants.DATA_DELIMITER;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.CHATTER_LEFT;
import static volfengaut.chatapp.message.server_to_client.ServerToClientMessageType.NO_PERMISSION_TO_BAN_USERS;

/**
 * Used to process ban messages send by admins to exclude a user from a chat room
 **/
@Controller
public class BanMessageProcessingController {
    
    @Autowired
    private IMessagingService messagingService;
    
    @Autowired
    private IActiveUsersService activeUsersService;
    
    @Autowired
    private IActiveRoomsService activeRoomsService;

    /**
     * Process the ban of the user
     **/
    public void processBan(Socket senderSocket, ChatterBannedMessage banMessage) {
        
        String senderName = banMessage.getAuthorName();
        String userNameToBan = banMessage.getRecipientName();
        
        // Get the sender-user from the context
        User sender = activeUsersService.getActiveUserByName(senderName);
        
        // Check if he has the permission to ban chatters
        if (!sender.hasPermission(Permission.REMOVE_CHATTER_FROM_CHAT_ROOM)) {
            
            // He has no such permission. Tell him so.
            messagingService.send(senderSocket, new ServerToClientMessage(NO_PERMISSION_TO_BAN_USERS, senderName, 
                    LocalDateTime.now(), null, activeUsersService.getSocketAddress(senderSocket)));
        } else {
            
            // He has such a permission. Update the banned user in the context and resend the ban message to him.
            activeUsersService.removeChatterFromHisRoom(userNameToBan);
            Socket bannedUserSocket = activeUsersService.getActiveUserSocket(userNameToBan);
            List<String> availableRooms = activeRoomsService.getNamesOfActiveRooms();
            banMessage.setAvailableOtherChatRooms(String.join(DATA_DELIMITER, availableRooms));
            messagingService.send(bannedUserSocket, banMessage);
            
            // Inform other chatters in this room that the chatter has left
            List<String> otherChatterNames = activeUsersService.getNamesOfChattersInRoom(banMessage.getChatRoomName());
            for (String otherChatterName : otherChatterNames) {
                Socket otherChatterSocket = activeUsersService.getActiveUserSocket(otherChatterName);
                if (otherChatterName.equals(senderName)) {
                    continue;
                }
                messagingService.send(otherChatterSocket, new ServerToClientMessage(CHATTER_LEFT, otherChatterName, 
                        LocalDateTime.now(), null, activeUsersService.getSocketAddress(otherChatterSocket)));
            }
        }
    }
}
