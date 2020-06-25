package volfengaut.chatapp.client.service;

import java.net.Socket;
import lombok.Data;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.client.api.service.IStatusService;

/**
 * A service to manage the current status of the user
 **/
@Data
@Service
public class StatusService implements IStatusService {
    
    private volatile String loginName;
    
    private volatile String currentRoomName;
    
    private Socket serverSocket;

}
