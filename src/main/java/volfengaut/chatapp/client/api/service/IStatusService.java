package volfengaut.chatapp.client.api.service;

import java.net.Socket;

public interface IStatusService {
    
    /**
     * Get the server socket
     **/
    Socket getServerSocket();
    
    /**
     * Set the server socket
     **/
    void setServerSocket(Socket serverSocket);
}
