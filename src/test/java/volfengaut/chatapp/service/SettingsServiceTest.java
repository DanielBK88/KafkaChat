package volfengaut.chatapp.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import volfengaut.chatapp.repository.SettingRepository;

import static org.junit.Assert.assertEquals;

public class SettingsServiceTest {

    private SettingService settingService;
    private SettingRepository settingsRepository;

    private EntityManagerFactory entityManagerFactory;
    
    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("daniil.it_course.unit_tests");
        
        settingService = new SettingService();
        settingsRepository = new SettingRepository();
        settingService.setRepository(settingsRepository);
        settingService.setEntityManagerFactory(entityManagerFactory);
    }
    
    @Test
    public void testSettings() {
        String name = "Some full name";
        String mail = "some.name@mail.ru";
        
        settingService.setFullName(name);
        settingService.setMailAddress(mail);
        
        assertEquals(name, settingService.getFullName());
        assertEquals(mail, settingService.getMailAddress());
    }
}
