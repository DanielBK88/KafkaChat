package volfengaut.chatapp.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.repository.RoleRepository;
import volfengaut.chatapp.repository.UserRepository;

import static volfengaut.chatapp.entity.role.Permission.SEND_PRIVATE_MESSAGES;
import static volfengaut.chatapp.entity.role.Permission.SEND_PUBLIC_MESSAGES;

public class CachingTest {

    private UserService userService;
    private UserRepository userRepository;

    private RoleService roleService;
    private RoleRepository roleRepository;

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
    }
    
    /**
     * В цикле достаем одну и ту же сущность (роль chatter) 10 раз.
     * Убеждаемся, что запросы на саму сущность Role не отсылаются в базу (она берется из кэша L2).
     * Отсылаются только запросы на ее Permission-ы, поскольку это более сложный запрос (с условием WHERE)
     * и его результаты не сохраныются в L2.
     **/
    @Test
    public void testRepeatedSelect() {
        // create chatter role
        UserRole chatterRole = new UserRole("chatter", SEND_PRIVATE_MESSAGES, SEND_PUBLIC_MESSAGES);
        roleService.addUserRole(chatterRole);
        
        // query the chatter 10 times
        System.out.println("Before selecting");
        for(int i = 1; i < 10; i++) {
            long startingTime = System.nanoTime();
            roleService.getRoleByName("chatter");
            long timeDelta = System.nanoTime() - startingTime;
            System.out.println("Select number " + i + " completed in " + timeDelta + " nanos.");
        }
        System.out.println("After selecting");
    }

}
