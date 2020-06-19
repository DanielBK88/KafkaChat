package volfengaut.chatapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.message.ChatMessage;
import volfengaut.chatapp.entity.role.Permission;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.ChatterBannedMessage;
import volfengaut.chatapp.repository.ChatRoomRepository;
import volfengaut.chatapp.repository.MessageRepository;
import volfengaut.chatapp.repository.RoleRepository;
import volfengaut.chatapp.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static volfengaut.chatapp.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permission.SEND_PUBLIC_MESSAGES;

/**
 * Data tests on creating and retrieving users
 **/
public class UserServiceTest {
    
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
    public void testSelectUsersByRole() {
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

        // select the chatters
        Collection<User> chatters = userService.getUsersByRole(chatterRole);
        
        assertEquals(2, chatters.size());
        assertTrue(chatters.contains(vasya));
        assertTrue(chatters.contains(petya));
        
        // select the admin
        Collection<User> admins = userService.getUsersByRole(adminRole);

        assertEquals(1, admins.size());
        assertTrue(admins.contains(admin));
    }
    
    @Test
    public void testSelectUsersByDateJoined() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create four chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);
        User vova = new User("Vova", chatterRole, LocalDate.parse("2014-01-14"));
        userService.addUser(vova);
        User vanya = new User("Vanya", chatterRole, LocalDate.parse("2017-02-12"));
        userService.addUser(vanya);
        
        // Select users joined after 2015
        Collection<User> users = userService.getUsersJoinedAfter(LocalDate.parse("2015-01-01"));

        assertEquals(3, users.size());
        assertTrue(users.contains(vasya));
        assertTrue(users.contains(petya));
        assertTrue(users.contains(vanya));
    }
    
    @Test
    public void testSelectAuthorsOfMessagesContainingText() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create three chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);
        User vova = new User("Vova", chatterRole, LocalDate.parse("2014-01-14"));
        userService.addUser(vova);

        // Create a room
        ChatRoom room = new ChatRoom("test-room", vova);
        roomService.createChatRoom(room);
        
        // Create some messages
        ChatMessage message1 = new ChatMessage("Vasya", "test-room", 
                LocalDateTime.parse("2019-03-19T10:15:30"),"Hello from Vasya to Petya", "Petya");
        ChatMessage message2 = new ChatMessage("Petya", "test-room",
                LocalDateTime.parse("2012-03-12T12:15:30"), "Hello from Petya to Vasya", "Vasya");
        ChatMessage message3 = new ChatMessage("Petya", "test-room", 
                LocalDateTime.parse("2012-03-12T10:11:20"), "Hello from Petya to Vova", "Vova");
        ChatMessage message4 = new ChatMessage("Vova", "test-room", 
                LocalDateTime.parse("2012-03-12T10:15:30"), "Goodbye from Vova to Petya", "Petya");
        
        messageService.addMessage(message1.toPersistableMessage(vasya, petya, room));
        messageService.addMessage(message2.toPersistableMessage(petya, vasya, room));
        messageService.addMessage(message3.toPersistableMessage(petya, vova, room));
        messageService.addMessage(message4.toPersistableMessage(vova, petya, room));

        // Select users joined after 2015
        Collection<User> users = userService.getAuthorsOfMessagesContaining("Hello");

        assertEquals(2, users.size());
        assertTrue(users.contains(vasya));
        assertTrue(users.contains(petya));
    }

    @Test
    public void testSelectBannedChatters() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);

        // create admin role
        UserRole adminRole = new UserRole("admin", Permission.values());
        roleService.addUserRole(adminRole);

        // create three chatters
        User vasya = new User("Vasya", chatterRole, LocalDate.parse("2018-05-14"));
        userService.addUser(vasya);
        User petya = new User("Petya", chatterRole, LocalDate.parse("2020-04-01"));
        userService.addUser(petya);
        User misha = new User("Misha", chatterRole, LocalDate.parse("2014-01-14"));
        userService.addUser(misha);

        // create two admins
        User admin1 = new User("Admin1", adminRole, LocalDate.parse("2015-11-23"));
        userService.addUser(admin1);
        User admin2 = new User("Admin2", adminRole, LocalDate.parse("2015-11-23"));
        userService.addUser(admin2);

        // Create two rooms
        ChatRoom room1 = new ChatRoom("test-room-1", admin1);
        roomService.createChatRoom(room1);
        ChatRoom room2 = new ChatRoom("test-room-2", admin2);
        roomService.createChatRoom(room2);
        
        // Create some ban messages
        ChatterBannedMessage vasyaBannedFromR1 = new ChatterBannedMessage(admin1.getLoginName(), room1.getName(),
                LocalDateTime.parse("2016-11-10T12:15:30"), "stupid chatter", vasya.getLoginName());
        ChatterBannedMessage mishaBannedFromR1 = new ChatterBannedMessage(admin2.getLoginName(), room1.getName(),
                LocalDateTime.parse("2017-12-08T12:14:30"), "another stupid chatter", misha.getLoginName());
        ChatterBannedMessage petyaBannedFromR2 = new ChatterBannedMessage(admin2.getLoginName(), room2.getName(),
                LocalDateTime.parse("2017-12-08T12:14:30"), "account not payed", petya.getLoginName());
        
        messageService.addMessage(vasyaBannedFromR1.toPersistableMessage(admin1, vasya, room1));
        messageService.addMessage(mishaBannedFromR1.toPersistableMessage(admin2, misha, room1));
        messageService.addMessage(petyaBannedFromR2.toPersistableMessage(admin2, petya, room2));

        // Select chatters banned from room 1
        Collection<User> users = userService.getChattersBannedFrom(room1);

        assertEquals(2, users.size());
        assertTrue(users.contains(vasya));
        assertTrue(users.contains(misha));
        
        // Select chatters banned from room 2
        users = userService.getChattersBannedFrom(room2);

        assertEquals(1, users.size());
        assertTrue(users.contains(petya));
        
        // Select chatters banned by admin 1
        users = userService.getChattersBannedBy(admin1);

        assertEquals(1, users.size());
        assertTrue(users.contains(vasya));
        
        // Select chatters banned by admin 2
        users = userService.getChattersBannedBy(admin2);

        assertEquals(2, users.size());
        assertTrue(users.contains(petya));
        assertTrue(users.contains(misha));
    }
    
}