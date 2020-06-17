package volfengaut.chatapp.controller;

import java.time.Duration;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.error.UnknownStatusChangeTypeException;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatMessage;
import volfengaut.chatapp.message.ChatterBannedMessage;
import volfengaut.chatapp.message.ChatterStatusChange;
import volfengaut.chatapp.message.ChatterStatusChangeMessage;
import volfengaut.chatapp.message.ExistingChatterInfoMessage;

import static volfengaut.chatapp.entity.role.Permisson.REMOVE_CHATTER_FROM_CHAT_ROOM;
import static volfengaut.chatapp.entity.role.Permisson.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permisson.SEND_PUBLIC_MESSAGES;
import static volfengaut.chatapp.message.ChatterStatusChange.ENTERING;
import static volfengaut.chatapp.message.ChatterStatusChange.LEAVING;

/**
 * The controller handling the actual conversation of a chatter in a chat room
 **/
@Controller
public class ChattingController {

    /**
     * The command to type to exit the chat room and the application
     **/
    private static final String EXIT_COMMAND = "exit";

    /**
     * When starting the user input with this String, followed by the name of another chatter,
     * then the message will be sent as a private message to this chatter only.
     * A message without any prefix will be sent to all chatters in the chat room (default).
     **/
    private static final String PRIVATE_MESSAGE_COMMAND_PREFIX = "-to ";
    
    private static final String CHATTER_BAN_MESSAGE_COMMAND_PREFIX = "-ban ";

    /**
     * The current chatter
     **/
    private User user;
    
    /**
     * The name of the current chatter (the user of the application)
     **/
    private String loginName;
    
    /**
     * The name of the chat room, where the current chat takes place.
     **/
    private String chatRoomName;

    /**
     * The consumer of Kafka messages
     **/
    private Consumer<String, AbstractMessage> messageConsumer;

    /**
     * The producer of Kafka messages
     **/
    @Autowired
    private Producer<String, AbstractMessage> messageProducer;

    @Autowired
    private volatile Scanner scanner;
    
    /**
     * Called after leaving the chat room
     **/
    @Autowired
    private SelectRoomController selectRoomController;

    private volatile AtomicInteger messageCount = new AtomicInteger(0);

    private volatile Set<String> otherChatters = ConcurrentHashMap.newKeySet();
    
    private boolean exitFlag = false;

    public void setMessageConsumer(Consumer<String, AbstractMessage> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void processChat(User user, ChatRoom room) {
        this.exitFlag = false;
        this.loginName = user.getLoginName();
        this.chatRoomName = room.getName();
        this.user = user;
        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                new ChatterStatusChangeMessage(loginName, ENTERING)));
        System.out.println("You entered the chat room as " + loginName);
        
        new Thread(this::readingLoop).start();
        writingLoop();
    }

    private void readingLoop() {
        ConsumerRecords<String, AbstractMessage> records;
        do {
            records = messageConsumer.poll(Duration.ofHours(100L));
            if (records != null & !records.isEmpty()) {
                for (ConsumerRecord<String, AbstractMessage> record : records) {
                    AbstractMessage message = record.value();
                    if (message == null || loginName.equals(message.getAuthorName())) {
                        continue;
                    }
                    if (message instanceof ExistingChatterInfoMessage) {
                        if (otherChatters.add(message.getAuthorName())) {
                            System.out.println("(" + message.getAuthorName() + " is already in this chat room.)");
                        }
                    } else if (message instanceof ChatterStatusChangeMessage) {
                        ChatterStatusChangeMessage statusChangeMessage = (ChatterStatusChangeMessage) message;
                        String authorName = statusChangeMessage.getAuthorName();
                        ChatterStatusChange statusChange = statusChangeMessage.getStatusChange();
                        if (ENTERING.equals(statusChange)) {
                            otherChatters.add(authorName);
                            messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                                    new ExistingChatterInfoMessage(loginName)));
                            System.out.println(authorName + " enters the chat room.");
                        } else if (LEAVING.equals(statusChange)) {
                            otherChatters.remove(authorName);
                            System.out.println(authorName + " leaves the chat room.");
                        } else {
                            throw new UnknownStatusChangeTypeException(statusChange);
                        }
                    } else if (message instanceof ChatMessage) {
                        ChatMessage chatMessage = (ChatMessage) message;
                        String author = chatMessage.getAuthorName();
                        String recipient = chatMessage.getRecipient();
                        if (recipient != null && !loginName.equals(recipient)) {
                            continue;
                        }
                        StringBuilder outputBuilder = new StringBuilder();
                        outputBuilder.append(author);
                        outputBuilder.append(recipient == null ? " (to all): " : " (to you): ");
                        outputBuilder.append(chatMessage.getMessageText());
                        System.out.println(outputBuilder.toString());
                    } else if (message instanceof ChatterBannedMessage) {
                        ChatterBannedMessage banMessage = (ChatterBannedMessage) message;
                        String recipient = banMessage.getBannedChatterName();
                        if (!loginName.equals(recipient)) {
                            continue;
                        }
                        System.out.println(message.getAuthorName() + " has banned you from this chat room! " 
                                + "Reason: " + banMessage.getReason());
                        exit();
                    }
                }
            }
        } while (!exitFlag);
    }

    private void writingLoop() {
        while (!exitFlag) {
            String command = scanner.nextLine();
            if (command.equals(EXIT_COMMAND)) {

                // Exit command
                exit();
            } else if (command.startsWith(PRIVATE_MESSAGE_COMMAND_PREFIX)) {
                
                // Private message sending command
                if (!user.hasPermission(SEND_PRIVATE_MESSAGES)) {
                    System.out.println("Sorry, you have no permission to send private messages!");
                    continue;
                }
                command = command.substring(PRIVATE_MESSAGE_COMMAND_PREFIX.length());
                boolean validRecipient = false;
                for (String recipient : otherChatters) {
                    if (command.startsWith(recipient)) {
                        String message = command.substring(recipient.length() + 1);
                        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                                new ChatMessage(loginName, message, recipient)));
                        validRecipient = true;
                        System.out.println("You (to " + recipient + "): " + message);
                        break;
                    }
                }
                if (!validRecipient) {
                    System.out.println("Unknown chatter!");
                }
            } else if (command.startsWith(CHATTER_BAN_MESSAGE_COMMAND_PREFIX)) {

                // Ban chatter command
                if (!user.hasPermission(REMOVE_CHATTER_FROM_CHAT_ROOM)) {
                    System.out.println("Sorry, you have no permission to ban chatters!");
                    continue;
                }
                command = command.substring(CHATTER_BAN_MESSAGE_COMMAND_PREFIX.length());
                boolean validRecipient = false;
                for (String recipient : otherChatters) {
                    if (command.startsWith(recipient)) {
                        String reason = command.substring(recipient.length() + 1);
                        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                                new ChatterBannedMessage(loginName, reason, recipient)));
                        validRecipient = true;
                        System.out.println("You banned the chatter " + recipient + " from this room");
                        break;
                    }
                }
                if (!validRecipient) {
                    System.out.println("Unknown chatter!");
                }
            } else {
                
                // Send public message command
                if (!user.hasPermission(SEND_PUBLIC_MESSAGES)) {
                    System.out.println("Sorry, you have no permission to send public messages!");
                    continue;
                }
                messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                        new ChatMessage(loginName, command, null)));
                System.out.println("You (to all): " + command);
            }

        }
    }

    private void exit() {
        exitFlag = true;
        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                new ChatterStatusChangeMessage(loginName, LEAVING)));
        messageProducer.close();
        messageConsumer.close();
        selectRoomController.processRoomSelection(user);
    }
    
}
