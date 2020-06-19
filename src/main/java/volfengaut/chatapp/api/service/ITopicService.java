package volfengaut.chatapp.api.service;

/**
 * A service for creating Kafka topics
 **/
public interface ITopicService {

    /**
     * Creates a new Kafka topic with the specified name, if it does not yet exist.
     * Assumes that the Kafka server and the ZooKeeper server are running.
     **/
    void createTopicIfAbsent(String name);
    
}
