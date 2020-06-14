package volfengaut.chatapp.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import volfengaut.chatapp.message.AbstractMessage;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Allows deserialization of AbstractMessage objects to allow using them as Kafka messages
 **/
public class MessageDeserializer implements Deserializer<AbstractMessage> {

    @Override
    public AbstractMessage deserialize(String topic, byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            AbstractMessage message = (AbstractMessage) objectInputStream.readObject();
            objectInputStream.close();
            return message;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AbstractMessage deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

}
