package volfengaut.chatapp.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static volfengaut.chatapp.constant.CommonConstants.SERVER_ADDRESS;
import static volfengaut.chatapp.constant.CommonConstants.SERVER_PORT;

@Configuration
@ComponentScan
public class ClientAppConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
    
    @Bean
    public Socket serverSocket() {
        try {
            return new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Bean
    public ObjectOutputStream serverOutputStream() {
        try {
            return new ObjectOutputStream(serverSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
