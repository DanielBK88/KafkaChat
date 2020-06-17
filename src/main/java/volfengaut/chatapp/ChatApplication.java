package volfengaut.chatapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import volfengaut.chatapp.controller.MainController;

/**
 * Starting point of the application.
 * Asks for a login name and a chat room name and enters the specified chat room (topic)
 * as a chatter with the given name.
 * If the topic does not yet exist, it will be created.
 * 
 * PREREQUISITES:
 * - The ZooKeeper server must be running on localhost:2181
 * - The Kafka server must be running on localhost:9092
 **/
public class ChatApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MainController mainController = context.getBean(MainController.class);
        
        mainController.process();
    }
}
