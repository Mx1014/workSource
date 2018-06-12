package com.everhomes.controller;

import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.PersistMessageRecordCommand;
import com.everhomes.util.SignatureHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WebSocketSessionProxy implements ApplicationListener<ContextRefreshedEvent> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebSocketSessionProxy.class);

    // @Value("${core.service.uri}")
    private String coreServiceUri = "http://10.1.10.37:8080/evh";

    // @Value("${border.app.key}")
    private String appKey = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";

    // @Value("${border.app.secret}")
    private String secretKey = "2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ";

    private static ConcurrentLinkedQueue<MessageRecordDto> queue = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<MessageRecordDto> getQueue() {
        return queue;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180525
    //@PostConstruct
    public void setup() {
        // 每次满一百条就持久化一次
        // Worker worker = new Worker(100);
        // worker.start();
    }

    public static void sendMessage(WebSocketSession session, WebSocketMessage message, String senderTag, String token) {
//        MessageRecordDto dto = new MessageRecordDto();
//        dto.setBody(message.getPayload().toString());
//        dto.setStatus(MessageRecordStatus.BORDER_ROUTE.getCode());
//        dto.setSenderTag(senderTag);

        /*switch (senderTag){
            case "NOTIFY REQUEST":
                Map actionData = (Map)StringHelper.fromJsonString(message.getPayload().toString(), Map.class);
                Map dataMap = (Map) actionData.get("response");
                List<Map> messages = (List<Map>) dataMap.get("messages");
                for (Map m : messages) {
                    MessageRecordDto dto = new MessageRecordDto();
                    dto.setBody(message.getPayload().toString());
                    dto.setStatus(MessageRecordStatus.BORDER_ROUTE.getCode());
                    dto.setSenderTag(senderTag);
                    Map extraInfo = (Map) m.get("extra");
                    dto.setIndexId(extraInfo.get("indexId") != null ? Long.valueOf(extraInfo.get("indexId").toString()) : 0);
                    dto.setDeviceId(token);
                    queue.offer(dto);
                }

                break;

            case "NOTIFY EVENT":
                MessageRecordDto dto1 = new MessageRecordDto();
                dto1.setBody(message.getPayload().toString());
                dto1.setStatus(MessageRecordStatus.BORDER_ROUTE.getCode());
                dto1.setSenderTag(senderTag);
                dto1.setDeviceId(token);
                queue.offer(dto1);
                break;

            default:
                MessageRecordDto dto2 = new MessageRecordDto();
                dto2.setBody(message.getPayload().toString());
                dto2.setStatus(MessageRecordStatus.BORDER_ROUTE.getCode());
                dto2.setSenderTag(senderTag);
                dto2.setSessionToken(token);
                queue.offer(dto2);
                break;
        }*/

        // 提交队列

        try {
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //消息持久化
    public void handleMessagePersist(List<MessageRecordDto> recordDtos) {

        List<PersistMessageRecordCommand> dtos = new ArrayList<>();

        recordDtos.forEach(r->{
            PersistMessageRecordCommand cmd = new PersistMessageRecordCommand();
            cmd.setMessageRecordDto(r.toString() != null ? r.toString() : "");
            cmd.setDeviceId(r.getDeviceId() != null ? r.getDeviceId().toString() : "");
            cmd.setSessionToken(r.getSessionToken() != null ? r.getSessionToken() : "");
            dtos.add(cmd);
        });

        Map<String, String> params = new HashMap<>();
//        params.put("messageRecordDto", record.toString());
//        params.put("sessionToken", record.getSessionToken());
//        params.put("deviceId", record.getDeviceId().toString());
        params.put("dtos", dtos.toString());

        this.restCall("/message/persistMessage", params, new ListenableFutureCallback<ResponseEntity<String>>() {

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


    public void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        params.put("appKey", this.appKey);
        String signature = SignatureHelper.computeSignature(params, this.secretKey);
        params.put("signature", signature);//Do not use UrlEncoder.encode when using http post in java.

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramMap.add(entry.getKey(), entry.getValue());
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(paramMap, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        future.addCallback(responseCallback);
    }

    private String getRestUri(String relativeUri) {
        StringBuffer sb = new StringBuffer(this.coreServiceUri);
        if (!this.coreServiceUri.endsWith("/"))
            sb.append("/");

        if (relativeUri.startsWith("/"))
            sb.append(relativeUri.substring(1));
        else
            sb.append(relativeUri);

        return sb.toString();
    }

    class Worker extends Thread {

        Worker(Integer threshold) {
            this.threshold = threshold;
        }

        private boolean finished = false;

        private Long tick = 0L;

        private Integer threshold = 0;

        private void stopMe() {
            this.finished = true;
        }

        @Override
        public void run() {
            try {
                List<MessageRecordDto> dtos = new ArrayList<>();

                for (; ; ) {
                    while (!queue.isEmpty() && !this.finished) {
                        MessageRecordDto record = queue.poll();
                        if (record != null)
                            dtos.add(record);
                        if (dtos.size() > threshold - 1 || System.currentTimeMillis() > tick + 10 * 1000) { //当取出的条数大于99或者距离上次持久化过去10S
                            tick = System.currentTimeMillis();
                            dtos.add(record);
                            handleMessagePersist(dtos);
                            dtos.clear();
                        } else if (this.finished) {
                            handleMessagePersist(dtos);
                            dtos.clear();
                            Thread.sleep(60*1000L);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
