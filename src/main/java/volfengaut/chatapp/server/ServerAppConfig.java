package volfengaut.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static volfengaut.chatapp.constant.CommonConstants.SERVER_PORT;

@ComponentScan
@Configuration
public class ServerAppConfig {
    
    @Bean
    public ServerSocket serverSocket() {
        try {
            return new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("daniil.it_course");
    }

}
