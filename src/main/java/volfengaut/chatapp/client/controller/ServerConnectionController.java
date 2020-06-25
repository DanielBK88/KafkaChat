package volfengaut.chatapp.client.controller;

import java.io.IOException;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class ServerConnectionController {
    
    @Autowired
    private ServerListeningController serverListeningController;

    @Autowired
    private CommandReadingController commandReadingController;
    
    @Autowired
    private Socket serverSocket;
    
    public void connect() {
        
        try {
            // Connect to the server
            System.out.println("Successfully connected to server: " + serverSocket.isConnected());

            //Start listening to server messages in a separate thread
            new Thread(() -> serverListeningController.listen(serverSocket)).start();

            // At the same time, start to read console commands.
            commandReadingController.process();
            
            // After leaving the command reading controller, close the socket and exit
            serverSocket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
