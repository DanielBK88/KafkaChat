package volfengaut.chatapp.server.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.role.Permission;
import volfengaut.chatapp.server.entity.role.UserRole;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.server.service.ChatRoomService;
import volfengaut.chatapp.server.service.MessageService;
import volfengaut.chatapp.server.service.RoleService;
import volfengaut.chatapp.server.service.UserService;

import static volfengaut.chatapp.constant.CommonConstants.ADMIN_ROLE_NAME;
import static volfengaut.chatapp.constant.CommonConstants.CHATTER_ROLE_NAME;
import static volfengaut.chatapp.server.entity.role.Permission.OPEN_CHAT_ROOM;
import static volfengaut.chatapp.server.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.server.entity.role.Permission.SEND_PUBLIC_MESSAGES;

/**
 * Fills the database with test data to allow running the application
 **/
@Controller
public class TestDataController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ChatRoomService roomService;
    
    @Autowired
    private MessageService messageService;
    
    public void fillTestData() {
        
        UserRole chatterRole = new UserRole(CHATTER_ROLE_NAME, SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES, OPEN_CHAT_ROOM);
        roleService.addUserRole(chatterRole);
        UserRole adminRole = new UserRole(ADMIN_ROLE_NAME, Permission.values());
        roleService.addUserRole(adminRole);
    }
}
