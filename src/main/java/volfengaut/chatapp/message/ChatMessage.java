package volfengaut.chatapp.message;

/**
 * An implementation of {@link AbstractMessage} representing a chat message
 **/
public class ChatMessage extends AbstractMessage {

    /**
     * The text of the message
     **/
    private final String messageText;
    
    /**
     * If specified, the message will be delivered as a private message to this recipient only.
     * If null, the message will be delivered to all chatters in the chat room.
     **/
    private final String recipient;
    
    public ChatMessage(String authorName, String messageText, String recipient) {
        super(authorName);
        this.messageText = messageText;
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "ChatMessage from " + getAuthorName() + " to " 
                + (recipient == null ? "all" : recipient) + ": " + messageText;
    }

}
