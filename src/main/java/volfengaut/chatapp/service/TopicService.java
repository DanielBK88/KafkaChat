package volfengaut.chatapp.service;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.service.ITopicService;

@Service
public class TopicService implements ITopicService {

    @Override
    public void createTopicIfAbsent(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The topic name should not be null or empty!");
        }
        try {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
            ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();

            Set<String> existingTopics =  listTopicsResult.names().get();
            if (existingTopics.contains(name)) {
                return;
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
    }

}