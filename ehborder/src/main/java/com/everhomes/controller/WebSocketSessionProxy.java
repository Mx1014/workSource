package com.everhomes.controller;

import com.everhomes.border.MessagePersistWorkerCopy;
import com.everhomes.rest.message.MessageRecordDto;

import com.everhomes.rest.message.MessageRecordStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WebSocketSessionProxy {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebSocketSessionProxy.class);
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

    public static void sendMessage(WebSocketSession session, WebSocketMessage message, String senderTag) {
        MessageRecordDto dto = new MessageRecordDto();
        dto.setBody(message.getPayload().toString());
        dto.setDstChannelToken(session.getId());
        dto.setStatus(MessageRecordStatus.BORDER_ROUTE.getCode());
        dto.setSenderTag(senderTag);
        LOGGER.debug(session.toString());
        LOGGER.debug(message.toString());
        queue.offer(dto);
        try {
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
