package volfengaut.chatapp.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import volfengaut.chatapp.api.repository.IChatRoomRepository;
import volfengaut.chatapp.api.service.IChatRoomService;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.entity.chat_room.ChatRoom;

@Service
public class ChatRoomService extends AbstractDataService implements IChatRoomService {

    @Setter(onMethod=@__({@Autowired}))
    private IChatRoomRepository repository;

    public void createChatRoom(ChatRoom room) {
        checkRoom(room);
        doInTransaction(r -> {
            repository.addChatRoom(r);
        }, room, repository);
    }

    @Override
    public ChatRoom getChatRoom(String name) {
        checkRoomName(name);
        return doInTransaction(n -> {
            return repository.getChatRoom(n);
        }, name, repository);
    }

    @Override
    public void deleteChatRoom(ChatRoom room) {
        checkRoom(room);
        doInTransaction(r -> {
            repository.deleteChatRoom(r);
        }, room, repository);
    }

}
