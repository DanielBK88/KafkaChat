package volfengaut.chatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.service.ChatRoomService;
import volfengaut.chatapp.service.RoleService;
import volfengaut.chatapp.service.UserService;

import static volfengaut.chatapp.entity.role.Permisson.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permisson.SEND_PUBLIC_MESSAGES;

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
        UserRole chatterRole = roleService.addUserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        UserRole adminRole = roleService.addUserRole("admin", Permisson.values());
        
        userService.addUser("Daniil", chatterRole);
        userService.addUser("Petya", chatterRole);
        User admin = userService.addUser("Vasya", adminRole);
        
        roomService.createChatRoom("test-room", admin);
        
        System.out.println("Test data creation finished successfully! :)");
    }
}
