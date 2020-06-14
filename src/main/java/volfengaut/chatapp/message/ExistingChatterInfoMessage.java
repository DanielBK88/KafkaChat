package volfengaut.chatapp.message;

/**
 * A message, informing a new entering chatter about chatters, which already exist in the given chat room.
 **/
public class ExistingChatterInfoMessage extends AbstractMessage {

    public ExistingChatterInfoMessage(String authorName) {
        super(authorName);
    }

    @Override
    public String toString() {
        return "Existing chatter Info: " + getAuthorName();
    }

}
