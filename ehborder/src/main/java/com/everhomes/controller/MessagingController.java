// @formatter:off
package com.everhomes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.everhomes.message.GreetingMessage;
import com.everhomes.message.HelloMessage;

@Controller
public class MessagingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingController.class);
    
    @Autowired
    SimpMessagingTemplate template;
    
    @MessageMapping("/greeting")
    @SendTo("/topic/greetings")
    public GreetingMessage greeting(HelloMessage message) throws Exception {
        LOGGER.info("Receive message. name in message: " + message.getName());

        GreetingMessage greetingMsg = new GreetingMessage("Hello " + message.getName());
        template.convertAndSend("/topic/greetings", greetingMsg);
        return greetingMsg;
    }
}
