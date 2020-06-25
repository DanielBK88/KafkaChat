package volfengaut.chatapp.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import volfengaut.chatapp.server.controller.ClientConnectionsWaitingController;

/**
 * The main class of the chat server application
 **/
public class ChatServer {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServerAppConfig.class);
        ClientConnectionsWaitingController mainController = context.getBean(ClientConnectionsWaitingController.class);

        mainController.process();
    }

}
