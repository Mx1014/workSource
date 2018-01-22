package com.everhomes.border;

import com.everhomes.controller.HttpRestCallProvider;
import com.everhomes.rest.messaging.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessagePersistWorker implements Runnable{

    @Autowired
    private HttpRestCallProvider httpRestCallProvider;

    private ConcurrentLinkedQueue<MessageDTO> queue;

    @Override
    public void run() {
        while (true){
            MessageDTO messageDto = queue.poll();
        }
    }


    private void messagePersist(){
        WebSocketSession oldSession = null;
        synchronized(this) {
            oldSession = this.tokenToSessionMap.get(cmd.getLoginToken());
        }
        if(oldSession != null && oldSession != session) {
            LOGGER.info("tearDown old sesssion, id=" + session.getId());
            tearDownSession(oldSession);
        }

        LOGGER.info("Handle register connection request, session=" + session.getId() + ", loginToken=" + cmd.getLoginToken());
        Map<String, String> params = new HashMap<>();
        params.put("borderId", String.valueOf(this.borderId));
        params.put("loginToken", cmd.getLoginToken());
        params.put("borderSessionId", session.getId());
    }
}
