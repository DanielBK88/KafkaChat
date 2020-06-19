package volfengaut.chatapp;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * Configuration of specific beans
 **/
@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    public Producer<String, AbstractMessage> producer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "volfengaut.chatapp.serializer.MessageSerializer");
        return new KafkaProducer<>(props);
    }

    @Bean
    @Lazy
    public Consumer<String, AbstractMessage> consumer(String loginName, String topicName) {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", loginName);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "volfengaut.chatapp.serializer.MessageDeserializer");
        Consumer<String, AbstractMessage> messageConsumer = new KafkaConsumer<>(props);
        messageConsumer.subscribe(Arrays.asList(topicName));
        messageConsumer.seekToBeginning(Collections.EMPTY_LIST);
        return messageConsumer;
    }
    
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("daniil.it_course");
    }
}
