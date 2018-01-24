package com.everhomes.controller;

import com.everhomes.border.MessagePersistWorkerCopy;
import com.everhomes.border.SchedulerConfig;
import com.everhomes.rest.message.MessageRecordDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WebSocketSessionProxy {
    @Autowired
    private MessagePersistWorkerCopy messagePersistWorker;

    private static ConcurrentLinkedQueue<MessageRecordDto> queue = new ConcurrentLinkedQueue<>();


    @Scheduled(fixedDelay = 5000)
    public void setup() {
        while (!queue.isEmpty()) {
            MessageRecordDto record = queue.poll();
            this.messagePersistWorker.handleMessagePersist(record);
        }
    }

    public static void sendMessage(WebSocketSession session, WebSocketMessage message) {
//        queue.offer(message);
        try {
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
