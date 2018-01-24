package com.everhomes.border;

import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.rpc.PduFrame;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WebSocketSessionProxy{

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private MessagePersistWorkerCopy messagePersistWorker;

    private static ConcurrentLinkedQueue<MessageRecordDto> queue = new ConcurrentLinkedQueue<>();


    @PostConstruct
    public void setup(){
        taskScheduler.scheduleAtFixedRate(()-> {
            while (!queue.isEmpty()){
                MessageRecordDto record = queue.poll();
                this.messagePersistWorker.handleMessagePersist(record);
            }
        }, 5*1000);
    }

    public static void sendMessage(WebSocketSession session, WebSocketMessage message){
//        queue.offer(message);
        try {
            synchronized (session){
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
