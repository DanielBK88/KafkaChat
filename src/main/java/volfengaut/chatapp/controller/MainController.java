package volfengaut.chatapp.controller;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;

/**
 * The starting controller (entry point of the application)
 **/
@Controller
public class MainController implements ApplicationContextAware {

    @Autowired
    private LoginController loginController;
    
    @Autowired
    private SelectRoomController selectRoomController;
    
    @Autowired
    private ChattingController chattingController;
    
    private ApplicationContext context;
    
    public void process() {
        User user = loginController.processLogin();
        while (true) {
            ChatRoom room = selectRoomController.processRoomSelection(user);
            chattingController.setMessageConsumer(
                    context.getBean(Consumer.class, user.getLoginName(), room.getName()));
            chattingController.processChat(user, room);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
