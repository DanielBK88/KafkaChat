package volfengaut.chatapp.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.server.api.service.IActiveUsersService;

@Service
public class ActiveUsersService implements IActiveUsersService {

    private Map<Socket, User> clientSockets = new HashMap<>();
    
    private Map<Socket, ObjectOutputStream> outputStreams = new HashMap<>();

    private Map<Socket, ObjectInputStream> inputStreams = new HashMap<>();
    
    @Override
    public User getActiveUserByName(String userName) {
        return clientSockets.values().stream()
                .filter(user -> user.getLoginName().equals(userName))
                .findAny().orElse(null);
    }

    @Override
    public User getActiveUserBySocket(Socket socket) {
        return clientSockets.get(socket);
    }

    @Override
    public Socket getActiveUserSocket(String userName) {
        return clientSockets.keySet().stream()
                .filter(socket -> clientSockets.get(socket).getLoginName().equals(userName))
                .findAny().orElse(null);
    }

    @Override
    public void setActiveUser(Socket clientSocket, User user) {
        clientSockets.put(clientSocket, user);
    }

    @Override
    public void removeActiveUser(Socket clientSocket) {
        clientSockets.remove(clientSocket);
    }

    @Override
    public List<String> getNamesOfChattersInRoom(String roomName) {
        return clientSockets.values().stream()
                .filter(user -> user.getCurrentRoom() != null && user.getCurrentRoom().getName().equals(roomName))
                .map(User::getLoginName)
                .collect(Collectors.toList());
    }

    @Override
    public void addChatterToRoom(ChatRoom room, String chatterName) {
        User user = getActiveUserByName(chatterName);
        if (user == null) {
            throw new IllegalArgumentException("There is no chatter with the provided name active: " + chatterName);
        }
        user.setCurrentRoom(room);
    }

    @Override
    public void removeChatterFromHisRoom(String chatterName) {
        addChatterToRoom(null, chatterName);
    }

    @Override
    public ObjectOutputStream getObjectOutputStreamForSocket(Socket socket) {

        try {        
            ObjectOutputStream cachedValue = outputStreams.get(socket);
            if (cachedValue == null) {
                cachedValue = new ObjectOutputStream(socket.getOutputStream());
                outputStreams.put(socket, cachedValue);
            }
            return cachedValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObjectInputStream getObjectInputStreamForSocket(Socket socket) {
        try {
            ObjectInputStream cachedValue = inputStreams.get(socket);
            if (cachedValue == null) {
                cachedValue = new ObjectInputStream(socket.getInputStream());
                inputStreams.put(socket, cachedValue);
            }
            return cachedValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSocketAddress(Socket socket) {
        return socket.getInetAddress() + ":" + socket.getPort();
    }

}
