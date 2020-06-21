package volfengaut.chatapp.controller;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.api.service.IMessageService;
import volfengaut.chatapp.api.service.IUserService;
import volfengaut.chatapp.entity.user.User;

/**
 * The controller handling the login process of a user
 **/
@Controller
public class LoginController {
    
    @Autowired
    private Scanner scanner;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private SignUpController signUpController;
    
    @Autowired
    private IMessageService messageService;

    public User processLogin() {
        while (true) {
            System.out.println("Please enter your login name!");
            String loginName = scanner.nextLine();
            User user = userService.getUserByName(loginName);
            if (user != null) {
                System.out.println(messageService.getWelcomeMessage("Russian"));
                return user;
            } else {
                user = signUpController.processSignUp(loginName);
                if (user != null) {
                    System.out.println(messageService.getWelcomeMessage("Russian"));
                    return user;
                }
            }
        }
    }
}
