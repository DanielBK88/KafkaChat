package volfengaut.chatapp.controller;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.api.service.IRoleService;
import volfengaut.chatapp.api.service.IUserService;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;

/**
 * The controller handling the sign-up process of a new user
 **/
@Controller
public class SignUpController {

    /**
     * Name of the chatter role
     **/
    private static final String CHATTER_ROLE_NAME = "chatter";

    /**
     * Command to sign up to the system (after failed login)
     **/
    private static final String SIGN_UP_COMMAND = "sign up";

    /**
     * Command to return to login (after failed login)
     **/
    private static final String LOGIN_COMMAND = "login";

    /**
     * Command to exit the system
     **/
    private static final String EXIT_COMMAND = "exit";
    
    @Autowired
    private Scanner scanner;
    
    @Autowired
    private IRoleService roleService;
    
    @Autowired
    private IUserService userService;

    public User processSignUp(String loginName) {
        System.out.println("You seem to be new to our system. Do you wish to sign up and chat with millions of other people?");
        while (true) {
            System.out.println("Type " + SIGN_UP_COMMAND + " to join us");
            System.out.println("Type " + LOGIN_COMMAND + " to return to login");
            System.out.println("Type " + EXIT_COMMAND + " to return to quit the system");
            String command = scanner.nextLine();
            switch (command) {
                case SIGN_UP_COMMAND:
                    UserRole chatterRole = roleService.getRoleByName(CHATTER_ROLE_NAME);
                    return userService.addUser(loginName, chatterRole);
                case LOGIN_COMMAND:
                    return null;
                case EXIT_COMMAND:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Oops, this was not a valid command!");
            }
        }
    }
}
