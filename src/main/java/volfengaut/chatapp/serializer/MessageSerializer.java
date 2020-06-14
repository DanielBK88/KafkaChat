package volfengaut.chatapp.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import volfengaut.chatapp.message.AbstractMessage;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Allows serialization of AbstractMessage objects to allow using them as Kafka messages
 **/
public class MessageSerializer implements Serializer<AbstractMessage> {

    @Override
    public byte[] serialize(String topic, AbstractMessage data) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(data);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't serialize object: " + data, e);
        }
    }

}
