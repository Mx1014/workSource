package com.everhomes.border;

import com.everhomes.controller.HttpRestCallProvider;
import com.everhomes.rest.messaging.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessagePersistWorker implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePersistWorker.class);

    @Autowired
    private HttpRestCallProvider httpRestCallProvider;

    private ConcurrentLinkedQueue<MessageDTO> queue;

    @Override
    public void run() {
        while (true){
            MessageDTO messageDto = queue.poll();
            this.handleMessagePersist(messageDto);
        }
    }

    //消息持久化
    private void handleMessagePersist(MessageDTO dto) {

        Map<String, String> params = new HashMap<>();
//        params.put("borderId", String.valueOf(this.borderId));
//        params.put("borderSessionId", session.getId());
        params.put("messageDto", dto.toJson());

        httpRestCallProvider.restCall("/message/persistMessage", params, new ListenableFutureCallback<ResponseEntity<String>>() {

            @Override
            public void onSuccess(ResponseEntity<String> result) {
                if(result.getStatusCode() == HttpStatus.OK) {

                }
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("call core server error=", ex);
            }
        });
    }

}
