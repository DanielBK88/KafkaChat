package volfengaut.chatapp.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.role.Permission;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.service.ChatRoomService;
import volfengaut.chatapp.service.RoleService;
import volfengaut.chatapp.service.UserService;

import static volfengaut.chatapp.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permission.SEND_PUBLIC_MESSAGES;

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
    
    public void fillTestData() {
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);
        UserRole adminRole = new UserRole("admin", Permission.values());
        roleService.addUserRole(adminRole);
        
        userService.addUser(new User("Daniil", chatterRole, LocalDate.now()));
        userService.addUser(new User("Petya", chatterRole, LocalDate.now()));
        User admin = new User("Vasya", adminRole, LocalDate.now());
        userService.addUser(admin);
        
        roomService.createChatRoom(new ChatRoom("test-room", admin));
        
        System.out.println("Test data creation finished successfully! :)");
    }
}
