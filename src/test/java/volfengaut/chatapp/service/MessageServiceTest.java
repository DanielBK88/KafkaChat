package volfengaut.chatapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
import volfengaut.chatapp.entity.message.StatusChangeMessageEntity;
import volfengaut.chatapp.entity.role.Permission;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.ChatMessage;
import volfengaut.chatapp.message.ChatterBannedMessage;
import volfengaut.chatapp.message.ChatterStatusChangeMessage;
import volfengaut.chatapp.repository.ChatRoomRepository;
import volfengaut.chatapp.repository.MessageRepository;
import volfengaut.chatapp.repository.RoleRepository;
import volfengaut.chatapp.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static volfengaut.chatapp.entity.message.MessageType.CHAT;
import static volfengaut.chatapp.entity.message.MessageType.STATUS_CHANGE;
import static volfengaut.chatapp.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permission.SEND_PUBLIC_MESSAGES;
import static volfengaut.chatapp.message.ChatterStatusChange.ENTERING;

/**
 * Data tests on creating and retrieving messages
 **/
public class MessageServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    private RoleService roleService;
    private RoleRepository roleRepository;

    private MessageService messageService;
    private MessageRepository messageRepository;

    private ChatRoomService roomService;
    private ChatRoomRepository roomRepository;

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("daniil.it_course.unit_tests");

        userService = new UserService();
        userRepository = new UserRepository();
        userService.setRepository(userRepository);
        userService.setEntityManagerFactory(entityManagerFactory);

        roleService = new RoleService();
        roleRepository = new RoleRepository();
        roleService.setRepository(roleRepository);
        roleService.setEntityManagerFactory(entityManagerFactory);

        messageService = new MessageService();
        messageRepository = new MessageRepository();
        messageService.setRepository(messageRepository);
        messageService.setEntityManagerFactory(entityManagerFactory);

        roomService = new ChatRoomService();
        roomRepository = new ChatRoomRepository();
        roomService.setRepository(roomRepository);
        roomService.setEntityManagerFactory(entityManagerFactory);
    }
    
    @Test
    public void testFindMessagesByAuthor() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create two chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);

        // Create a room
        ChatRoom room = new ChatRoom("test-room", vasya);
        roomService.createChatRoom(room);

        // Create some messages
        ChatMessage message1 = new ChatMessage("Vasya", "test-room",
                LocalDateTime.parse("2019-03-19T10:15:30"),"Hello from Vasya to Petya", "Petya");
        ChatMessage message2 = new ChatMessage("Petya", "test-room",
                LocalDateTime.parse("2012-03-12T12:15:30"), "Hello from Petya to Vasya", "Vasya");
        ChatMessage message3 = new ChatMessage("Vasya", "test-room",
                LocalDateTime.parse("2012-03-12T10:11:20"), "Goodbye from Vasya to Petya", "Petya");
        ChatMessage message4 = new ChatMessage("Petya", "test-room",
                LocalDateTime.parse("2012-03-12T10:15:30"), "Goodbye from Petya to Vasya", "Vasya");

        messageService.addMessage(message1.toPersistableMessage(vasya, petya, room));
        messageService.addMessage(message2.toPersistableMessage(petya, vasya, room));
        messageService.addMessage(message3.toPersistableMessage(vasya, petya, room));
        messageService.addMessage(message4.toPersistableMessage(petya, vasya, room));
        
        // Select messages sent by Vasya
        Collection<ChatMessageEntity> messages = messageService.findMessagesSendBy(vasya);
        
        assertEquals(2, messages.size());
        assertTrue(messages.stream().map(ChatMessageEntity::getText).anyMatch(t -> t.equals("Hello from Vasya to Petya")));
        assertTrue(messages.stream().map(ChatMessageEntity::getText).anyMatch(t -> t.equals("Goodbye from Vasya to Petya")));

        // Select messages sent by Petya
        messages = messageService.findMessagesSendBy(petya);

        assertEquals(2, messages.size());
        assertTrue(messages.stream().map(ChatMessageEntity::getText).anyMatch(t -> t.equals("Hello from Petya to Vasya")));
        assertTrue(messages.stream().map(ChatMessageEntity::getText).anyMatch(t -> t.equals("Goodbye from Petya to Vasya")));
    }
    
    @Test
    public void findMessagesOfType() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create two chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);

        // Create a room
        ChatRoom room = new ChatRoom("test-room", vasya);
        roomService.createChatRoom(room);

        // Create some messages
        ChatMessage message1 = new ChatMessage("Vasya", "test-room",
                LocalDateTime.parse("2019-03-19T10:15:30"),"Hello from Vasya to Petya", "Petya");
        ChatMessage message2 = new ChatMessage("Petya", "test-room",
                LocalDateTime.parse("2012-03-12T12:15:30"), "Hello from Petya to Vasya", "Vasya");
        ChatterStatusChangeMessage message3 = new ChatterStatusChangeMessage("Vasya", "test-room",
                LocalDateTime.parse("2012-05-11T12:11:30"), ENTERING);

        messageService.addMessage(message1.toPersistableMessage(vasya, petya, room));
        messageService.addMessage(message2.toPersistableMessage(petya, vasya, room));
        messageService.addMessage(message3.toPersistableMessage(vasya, null, room));
        
        // Select messages of type chat message
        Collection<ChatMessageEntity> chatMessages = messageService.findMessagesOfType(ChatMessageEntity.class);

        assertEquals(2, chatMessages.size());

        // Select messages of type status change message
        Collection<StatusChangeMessageEntity> statusChangeMessages = 
                messageService.findMessagesOfType(StatusChangeMessageEntity.class);

        assertEquals(1, statusChangeMessages.size());
    }
    
    @Test
    public void testCountOfBansForUser() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create admin role
        UserRole adminRole = new UserRole("admin", Permission.values());
        roleService.addUserRole(adminRole);

        // create two chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);

        // create one admin
        User admin = new User("Vova", adminRole, LocalDate.parse("2015-11-23"));
        userService.addUser(admin);

        // Create a room
        ChatRoom room = new ChatRoom("test-room", vasya);
        roomService.createChatRoom(room);
        
        // Create ban messages
        ChatterBannedMessage vasyaBanned1 = new ChatterBannedMessage(admin.getLoginName(), room.getName(),
                LocalDateTime.parse("2016-11-23T12:15:30"), "I don't know why ...", vasya.getLoginName());
        ChatterBannedMessage vasyaBanned2 = new ChatterBannedMessage(admin.getLoginName(), room.getName(),
                LocalDateTime.parse("2016-12-21T12:19:30"), "I don't know why ...", vasya.getLoginName());
        ChatterBannedMessage vasyaBanned3 = new ChatterBannedMessage(admin.getLoginName(), room.getName(),
                LocalDateTime.parse("2013-11-23T14:01:20"), "I don't know why ...", vasya.getLoginName());
        ChatterBannedMessage petyaBanned1 = new ChatterBannedMessage(admin.getLoginName(), room.getName(),
                LocalDateTime.parse("2014-11-21T14:04:20"), "I don't know why ...", petya.getLoginName());
        ChatterBannedMessage petyaBanned2 = new ChatterBannedMessage(admin.getLoginName(), room.getName(),
                LocalDateTime.parse("2018-11-21T11:04:20"), "I don't know why ...", petya.getLoginName());

        messageService.addMessage(vasyaBanned1.toPersistableMessage(admin, vasya, room));
        messageService.addMessage(vasyaBanned2.toPersistableMessage(admin, vasya, room));
        messageService.addMessage(vasyaBanned3.toPersistableMessage(admin, vasya, room));

        messageService.addMessage(petyaBanned1.toPersistableMessage(admin, petya, room));
        messageService.addMessage(petyaBanned2.toPersistableMessage(admin, petya, room));
        
        int vasyaBansCount = messageService.countBansOfUser(vasya);
        int petyaBansCount = messageService.countBansOfUser(petya);
        
        assertEquals(3, vasyaBansCount);
        assertEquals(2, petyaBansCount);
    }
}
