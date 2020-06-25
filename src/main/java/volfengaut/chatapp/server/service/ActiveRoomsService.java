package volfengaut.chatapp.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.server.api.service.IActiveRoomsService;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;

@Service
public class ActiveRoomsService implements IActiveRoomsService {

    private List<ChatRoom> rooms = new ArrayList<>();
    
    @Override
    public List<String> getNamesOfActiveRooms() {
        return rooms.stream().map(ChatRoom::getName).collect(Collectors.toList());
    }

    @Override
    public ChatRoom getActiveRoom(String roomName) {
        return rooms.stream().filter(room -> room.getName().equals(roomName)).findAny().orElse(null);
    }

    @Override
    public void addActiveRoom(ChatRoom room) {
        rooms.add(room);
    }

    @Override
    public void removeActiveRoom(ChatRoom room) {
        rooms.remove(room);
    }

}
