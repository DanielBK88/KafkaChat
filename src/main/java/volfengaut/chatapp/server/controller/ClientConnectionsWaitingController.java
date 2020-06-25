package volfengaut.chatapp.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Main controller of the server application.
 * Waits for connections of clients and starts new threads for the communication with those clients.
 **/
@Controller
public class ClientConnectionsWaitingController {
    
    @Autowired
    private ServerSocket serverSocket;
    
    @Autowired
    private ClientCommunicationController clientCommunicationController;

    @Autowired
    private ExecutorService executorService;

    public void process() {
        System.out.println("Server running :)");
        while (true) {
            
            // Wait for a new client to connect
            try {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client.getPort());
                
                // Submit an asynchronous task to communicate with this client
                executorService.submit(() -> {
                    clientCommunicationController.communicate(client);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
