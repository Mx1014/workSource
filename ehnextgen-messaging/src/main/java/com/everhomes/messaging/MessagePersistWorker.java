//package com.everhomes.messaging;
//
//import com.everhomes.rest.messaging.MessageDTO;
//import com.everhomes.util.SignatureHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import org.springframework.web.client.AsyncRestTemplate;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//@Component
//public class MessagePersistWorker implements Runnable {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePersistWorker.class);
//
//    @Value("${core.service.uri}")
//    private String coreServiceUri;
//
//    @Value("${border.app.key}")
//    private String appKey;
//
//    @Value("${border.app.secret}")
//    private String secretKey;
//
//
//    private ConcurrentLinkedQueue<MessageDTO> queue;
//
//    @Override
//    public void run() {
//        while (true) {
//            MessageDTO messageDto = queue.poll();
//            this.handleMessagePersist(messageDto);
//        }
//    }
//
//    //消息持久化
//    public void handleMessagePersist(MessageDTO dto) {
//
//        Map<String, String> params = new HashMap<>();
////        params.put("borderId", String.valueOf(this.borderId));
////        params.put("borderSessionId", session.getId());
//        params.put("messageDto", dto.toJson());
//
//        this.restCall("/message/persistMessage", params, new ListenableFutureCallback<ResponseEntity<String>>() {
//
//            @Override
//            public void onSuccess(ResponseEntity<String> result) {
//                if (result.getStatusCode() == HttpStatus.OK) {
//                    System.out.println("persist message success!");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                LOGGER.error("call core server error=", ex);
//            }
//        });
//    }
//
//
//    public void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) {
//        AsyncRestTemplate template = new AsyncRestTemplate();
//        String url = getRestUri(cmd);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        params.put("appKey", this.appKey);
//        String signature = SignatureHelper.computeSignature(params, this.secretKey);
//        params.put("signature", signature);//Do not use UrlEncoder.encode when using http post in java.
//
//        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            paramMap.add(entry.getKey(), entry.getValue());
//        }
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(paramMap, headers);
//        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        future.addCallback(responseCallback);
//    }
//
//    private String getRestUri(String relativeUri) {
//        StringBuffer sb = new StringBuffer(this.coreServiceUri);
//        if (!this.coreServiceUri.endsWith("/"))
//            sb.append("/");
//
//        if (relativeUri.startsWith("/"))
//            sb.append(relativeUri.substring(1));
//        else
//            sb.append(relativeUri);
//
//        return sb.toString();
//    }
//
//
//    public void setQueue(ConcurrentLinkedQueue<MessageDTO> queue) {
//        this.queue = queue;
//    }
//}
