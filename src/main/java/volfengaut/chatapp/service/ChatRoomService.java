package volfengaut.chatapp.service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import volfengaut.chatapp.api.repository.IChatRoomRepository;
import volfengaut.chatapp.api.service.IChatRoomService;
import java.util.Properties;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;

@Service
public class ChatRoomService implements IChatRoomService {
    
    @Autowired
    private IChatRoomRepository repository;

    public ChatRoom createChatRoom(String name, User creator) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The room name should not be null or empty!");
        } else if (creator == null) {
            throw new IllegalArgumentException("The creator should not be null or empty!");
        }
        try {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
            ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();

            Set<String> existingTopics =  listTopicsResult.names().get();
            if (existingTopics.contains(name)) {
                throw new IllegalStateException("Tried to create a chat room for which a Kafka topic already exists!");
            }
            
            CreateTopicsResult result = kafkaAdminClient.createTopics(
                    Stream.of(name).map(
                            n -> new NewTopic(n, 3, (short) 1)
                    ).collect(Collectors.toList())
            );
            result.all().get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return repository.addChatRoom(name, creator);
    }

    @Override
    public ChatRoom getChatRoom(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The room name should not be null or empty!");
        }
        return repository.getChatRoom(name);
    }

    @Override
    public boolean deleteChatRoom(ChatRoom room) {
        if (room == null) {
            throw new IllegalArgumentException("The room should not be null!");
        }
        return repository.deleteChatRoom(room);
    }

}
