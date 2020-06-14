package volfengaut.chatapp.message;

/**
 * A message, which informs about a status change, which happened to it's author
 * (he entered or left the chat room)
 **/
public class ChatterStatusChangeMessage extends AbstractMessage {
    
    /**
     * The status change to inform other chatters about
     **/
    private ChatterStatusChange statusChange;

    public ChatterStatusChangeMessage(String authorName, ChatterStatusChange statusChange) {
        super(authorName);
        this.statusChange = statusChange;
    }

    public ChatterStatusChange getStatusChange() {
        return statusChange;
    }

    @Override
    public String toString() {
        return "Status change message: " + getAuthorName() + " " + statusChange;
    }

}
