package volfengaut.chatapp;

import java.util.Scanner;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import volfengaut.chatapp.chatter.Chatter;

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
        Chatter chatter = context.getBean(Chatter.class);
        Scanner scanner = context.getBean(Scanner.class);
        System.out.println("Please choose a chat room name:");
        String roomName = scanner.nextLine();
        chatter.setChatRoomName(roomName);
        System.out.println("Please choose a login name:");
        String loginName = scanner.nextLine();
        chatter.setLoginName(loginName);
        chatter.setMessageConsumer(context.getBean(Consumer.class, loginName, roomName));
        chatter.start();
    }

}
