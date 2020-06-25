package volfengaut.chatapp.client.controller.message_processing;

import org.springframework.stereotype.Controller;
import volfengaut.chatapp.message.client_to_client.ChatMessage;

/**
 * Used to process incoming chat messages
 **/
@Controller
public class ChatMessageProcessingController {

    public void process(ChatMessage message) {
        String recipient = message.getRecipientName();
        String author = message.getAuthorName();
        System.out.println(author + " (to " + (recipient == null ? "all" : "you") + "): " + message.getMessageText());
    }
    
}
