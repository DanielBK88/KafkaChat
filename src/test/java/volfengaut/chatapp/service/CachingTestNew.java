package volfengaut.chatapp.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import volfengaut.chatapp.entity.WelcomeMessage;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.repository.MessageRepository;
import volfengaut.chatapp.repository.RoleRepository;
import volfengaut.chatapp.repository.UserRepository;

import static volfengaut.chatapp.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permission.SEND_PUBLIC_MESSAGES;

public class CachingTestNew {

    private MessageService messageService;
    private MessageRepository messageRepository;

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("daniil.it_course.unit_tests");

        messageService = new MessageService();
        messageRepository = new MessageRepository();
        messageService.setRepository(messageRepository);
        messageService.setEntityManagerFactory(entityManagerFactory);
    }
    
    /**
     * Retrieve 10 times the same entity (welcome message) by its ID (language).
     * Make sure, that the entity is fetched from db only once. The subsequent times it is fetched from L2 cache.
     **/
    @Test
    public void testRepeatedSelect() {
        messageService.setWelcomeMessage(new WelcomeMessage("Russian", "Добро Пожаловать!"));
        
        // query the welcome message 10 times
        System.out.println("Before selecting");
        for(int i = 1; i < 10; i++) {
            long startingTime = System.currentTimeMillis();
            System.out.println(messageService.getWelcomeMessage("Russian"));
            long timeDelta = System.currentTimeMillis() - startingTime;
            System.out.println("Select number " + i + " completed in " + timeDelta + " ms.");
        }
        System.out.println("After selecting");
    }

}
