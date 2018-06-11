package com.everhomes.controller;

import com.everhomes.rest.messaging.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommonHandshakeInterceptor implements HandshakeInterceptor, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonHandshakeInterceptor.class);

    @Autowired
    private ConcurrentLinkedQueue<MessageDTO> queue;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private HttpRestCallProvider httpRestCallProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180525
    //@PostConstruct
    public void setup() {
//        String triggerName = StatTransactionScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
//        String jobName = triggerName;
//        String cronExpression = configurationProvider.getValue(StatTransactionConstant.STAT_CRON_EXPRESSION, StatTransactionScheduleJob.CRON_EXPRESSION);
//        //启动定时任务
//        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, StatTransactionScheduleJob.class, null);

        taskScheduler.scheduleAtFixedRate(() -> {
            while (!queue.isEmpty()) {
                MessageDTO messageDto = queue.poll();
                this.handleMessagePersist(messageDto);
            }
        }, 5 * 1000);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        System.out.println("beforeHandshake");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("afterHandshake");
    }

    //消息持久化
    public void handleMessagePersist(MessageDTO dto) {

        Map<String, String> params = new HashMap<>();
//        params.put("borderId", String.valueOf(this.borderId));
//        params.put("borderSessionId", session.getId());
        params.put("messageDto", dto.toJson());

        this.httpRestCallProvider.restCall("/message/persistMessage", params, new ListenableFutureCallback<ResponseEntity<String>>() {

            @Override
            public void onSuccess(ResponseEntity<String> result) {
                if (result.getStatusCode() == HttpStatus.OK) {
                    System.out.println("persist message success!");
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("call core server error=", ex);
            }
        });
    }
}
