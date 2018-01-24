package com.everhomes.border;

import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.util.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessagePersistWorkerCopy implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePersistWorkerCopy.class);

    //    @Value("${core.service.uri}")
    private String coreServiceUri = "http://10.1.10.37:8080/evh";

    //    @Value("${border.app.key}")
    private String appKey = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";

    //    @Value("${border.app.secret}")
    private String secretKey = "2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ";


    private ConcurrentLinkedQueue<MessageRecordDto> queue;

    @Override
    public void run() {
        while (true) {
            MessageRecordDto record = queue.poll();
            this.handleMessagePersist(record);
        }
    }

    //消息持久化
    public void handleMessagePersist(MessageRecordDto record) {

        Map<String, String> params = new HashMap<>();
//        params.put("borderId", String.valueOf(this.borderId));
//        params.put("borderSessionId", session.getId());
        params.put("messageRecordDto", record.toString());

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


    public void setQueue(ConcurrentLinkedQueue<MessageRecordDto> queue) {
        this.queue = queue;
    }
}
