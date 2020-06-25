package volfengaut.chatapp.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import volfengaut.chatapp.client.controller.ServerConnectionController;

/**
 * Main class of the chat client application
 **/
public class ChatClient {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientAppConfig.class);
        ServerConnectionController mainController = context.getBean(ServerConnectionController.class);
        
        mainController.connect();
    }
}
