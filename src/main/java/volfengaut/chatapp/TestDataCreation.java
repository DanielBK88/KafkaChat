package volfengaut.chatapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import volfengaut.chatapp.controller.TestDataController;

/**
 * Application for creating test data to run the main application
 **/
public class TestDataCreation {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TestDataController testDataController = context.getBean(TestDataController.class);

        testDataController.fillTestData();
    }

}
