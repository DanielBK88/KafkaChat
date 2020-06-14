package volfengaut.chatapp.chatter;

import volfengaut.chatapp.api.IChatRoomService;
import volfengaut.chatapp.error.UnknownStatusChangeTypeException;
import java.time.Duration;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatMessage;
import volfengaut.chatapp.message.ChatterStatusChange;
import volfengaut.chatapp.message.ChatterStatusChangeMessage;
import volfengaut.chatapp.message.ExistingChatterInfoMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static volfengaut.chatapp.message.ChatterStatusChange.ENTERING;
import static volfengaut.chatapp.message.ChatterStatusChange.LEAVING;

/**
 * A participant of a chat in a specific chat room (topic) with a specific login name 
 **/
@Component
public class Chatter {
    
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

    private String loginName;
    private String chatRoomName; // aka topic name
    
    private Consumer<String, AbstractMessage> messageConsumer;

    @Autowired
    private Producer<String, AbstractMessage> messageProducer;
    
    @Autowired
    private IChatRoomService chatRoomService;
    
    @Autowired
    private volatile Scanner scanner;
    
    private volatile AtomicInteger messageCount = new AtomicInteger(0);
    
    private volatile Set<String> otherChatters = ConcurrentHashMap.newKeySet();
    
    private boolean exitFlag = false;
    
    public void start() {
        new Thread(this::writingLoop).start();
        new Thread(this::readingLoop).start();
        this.enter();
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        chatRoomService.createChatRoom(chatRoomName);
    }

    public void setMessageConsumer(
            Consumer<String, AbstractMessage> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    private void readingLoop() {
        ConsumerRecords<String, AbstractMessage> records;
        do {
            records = messageConsumer.poll(Duration.ofHours(100L));
            if (records != null & !records.isEmpty()) {
                for (ConsumerRecord<String, AbstractMessage> record : records) {
                    AbstractMessage message = record.value();
                    //System.out.println("Time: " + System.currentTimeMillis() + " my name: " + loginName + " received message: " + message + ", other known chatters: " + otherChatters);
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
                            //System.out.println("Time: " + System.currentTimeMillis() + ": " + loginName + " sends ExistingChatterInfoMessage");
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
                    }
                }
            } 
        } while (!exitFlag);
    }
    
    private void writingLoop() {
        while (!exitFlag) {
            String command = scanner.nextLine();
            if (command.equals(EXIT_COMMAND)) {
                exit();
            } else {
                if (command.startsWith(PRIVATE_MESSAGE_COMMAND_PREFIX)) {
                    command = command.substring(PRIVATE_MESSAGE_COMMAND_PREFIX.length());
                    boolean validRecipient = false;
                    for (String recipient : otherChatters) {
                        if (command.startsWith(recipient)) {
                            String message = command.substring(recipient.length() + 1);
                            messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                                    new ChatMessage(loginName, message, recipient)));
                            //System.out.println("Time: " + System.currentTimeMillis() + ": " + loginName + " sends private chat message");
                            validRecipient = true;
                            System.out.println("You (to " + recipient + "): " + message);
                            break;
                        }
                    }
                    if (!validRecipient) {
                        System.out.println("Unknown chatter!");
                    }
                } else {
                    messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                            new ChatMessage(loginName, command, null)));
                    //System.out.println("Time: " + System.currentTimeMillis() + ": " + loginName + " sends public chat message");
                    System.out.println("You (to all): " + command);
                }
            }
        }
    }
    
    private void enter() {
        exitFlag = false;
        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                new ChatterStatusChangeMessage(loginName, ENTERING)));
        //System.out.println("Time: " + System.currentTimeMillis() + ": " + loginName + " sends entering message");
        System.out.println("You entered the chat room as " + loginName);
    }
    
    private void exit() {
        exitFlag = true;
        messageProducer.send(new ProducerRecord<>(chatRoomName, (messageCount.incrementAndGet() + loginName),
                new ChatterStatusChangeMessage(loginName, LEAVING)));
        //System.out.println("Time: " + System.currentTimeMillis() + ": " + loginName + " sends leaving message");
        messageProducer.close();
        messageConsumer.close();
        System.exit(0);
    }
    


}
