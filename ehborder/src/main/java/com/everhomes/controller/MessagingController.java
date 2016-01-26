// @formatter:off
package com.everhomes.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.message.GreetingMessage;
import com.everhomes.message.HelloMessage;
import com.everhomes.util.StringHelper;

@Controller
public class MessagingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingController.class);
    
    @Autowired
    SimpMessagingTemplate template;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Value("${admin.auth:@$##fyhbuRR45678}")
    private String auth;
    
    @MessageMapping("/greeting")
    @SendTo("/topic/greetings")
    public GreetingMessage greeting(HelloMessage message) throws Exception {
        LOGGER.info("Receive message. name in message: " + message.getName());

        GreetingMessage greetingMsg = new GreetingMessage("Hello " + message.getName());
        template.convertAndSend("/topic/greetings", greetingMsg);
        return greetingMsg;
    }
    
    @RequestMapping("/admin/getDeviceSessions")
    public String getDeviceSessions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String auth = request.getParameter("auth");
        
        Map<String, Object> map = new HashMap<String, Object>();
        if(this.auth.equals(auth)) {
            ClientWebSocketHandler clientWebSocketHandler = applicationContext.getBean(ClientWebSocketHandler.class);
            PusherWebSocketHandler pusherWebSocketHandler = applicationContext.getBean(PusherWebSocketHandler.class);
            
            Map<String, Object> tokenMap = null;
            if(pusherWebSocketHandler != null) {
                tokenMap = clientWebSocketHandler.getOnlineTokens();
            } else {
                LOGGER.error("ClientWebSocketHandler not fond");
            }
            Map<String, Object> deviceMap = null;
            if(pusherWebSocketHandler != null) {
                deviceMap = pusherWebSocketHandler.getOnlineDevices();
            } else {
                LOGGER.error("PusherWebSocketHandler not fond");
            }
            
            map.put("onlineTokens", tokenMap);
            map.put("onlineDevices", deviceMap);
        }
        
        PrintWriter writer = null;
        try {
            response.addHeader("Content-Type", "application/json");
            writer = response.getWriter();
            writer.write(StringHelper.toJsonString(map));
        } catch(Exception e) {
            LOGGER.error("Failed to write response, map=" + map, e);
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch(Exception e) {
                    LOGGER.error("Failed to close response writer, map=" + map, e);
                }
            }
        }
        
        return null;
    }
}
