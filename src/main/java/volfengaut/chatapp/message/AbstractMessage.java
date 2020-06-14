package volfengaut.chatapp.message;

import java.io.Serializable;

/**
 * An abstract chat message to be send via Kafka within a chat room.
 **/
public abstract class AbstractMessage implements Serializable {

    /**
     * The name of the author of this message
     **/
    private final String authorName;

    public AbstractMessage(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

}
