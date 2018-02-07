package com.everhomes.util;

import com.atomikos.util.DateHelper;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.PersistMessageRecordCommand;
import com.everhomes.rest.messaging.MessageDTO;
import javafx.concurrent.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MessagePersistWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePersistWorker.class);

    //    @Value("${core.service.uri}")
    private String coreServiceUri = "http://10.1.10.37:8080/evh";

    //    @Value("${border.app.key}")
    private String appKey = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";

    //    @Value("${border.app.secret}")
    private String secretKey = "2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ";

    private static volatile ConcurrentLinkedQueue<MessageRecordDto> queue = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<MessageRecordDto> getQueue() {
        return queue;
    }
//
//    Lock lock = new ReentrantLock();
//
//    private Condition condition1 = lock.newCondition();
//
//    private Condition condition2 = lock.newCondition();
//
//    private Condition condition3 = lock.newCondition();
//
//    private Condition condition4 = lock.newCondition();
//
//    private static ThreadLocal state;
//
//
//    private final static CountDownLatch countDownLatch1 = new CountDownLatch(1);
//    private final static CountDownLatch countDownLatch2 = new CountDownLatch(2);
//    private final static CountDownLatch countDownLatch3 = new CountDownLatch(3);
//    private final static CountDownLatch countDownLatch4 = new CountDownLatch(4);

    @Autowired
    private TaskScheduler taskScheduler;

    @PostConstruct
    public void setup() {
//        ArrayList<MessagePersistWorker.Worker> workers = new ArrayList<>();
//        Thread t1 = new Thread(new Worker(2500, condition1, countDownLatch1));
//        t1.setName("Pesist-1");
//        t1.start();
//
//        Thread t2 = new Thread(new Worker(5000, condition2, countDownLatch2));
//        t2.setName("Pesist-2");
//        t2.start();
//
//        Thread t3 = new Thread(new Worker(7500, condition2, countDownLatch3));
//        t3.setName("Pesist-3");
//        t3.start();
//
//        Thread t4 = new Thread(new Worker(8000, condition2, countDownLatch4));
//        t4.setName("Pesist-4");
//        t4.start();

//
//        Worker worker1 = new Worker(2500);
//        Worker worker2 = new Worker(5000);
//        Worker worker3 = new Worker(7500);
//        Worker worker4 = new Worker(10000);

//        try {
//            int i = 0;
//            for (; ; ) {
//                i = queue.size();
//                if (i > 0 && i < 2500) {
//                    worker1.start();
//                    worker2.stopMe();
//                    worker3.stopMe();
//                    worker4.stopMe();
//                }
//                if (i > 2500 && i < 5000) {
//                    worker1.start();
//                    worker2.start();
//                    worker3.stopMe();
//                    worker4.stopMe();
//                }
//                if (i > 5000 && i < 7500) {
//                    worker1.start();
//                    worker2.start();
//                    worker3.start();
//                    worker4.stopMe();
//                }
//                if (i > 7500 && i < 10000) {
//                    worker1.start();
//                    worker2.start();
//                    worker3.start();
//                    worker4.start();
//                }
//
//                Thread.sleep(5000L);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        while (true){
            // 每次满一百条就持久化一次
            Worker worker = new Worker(100);
            worker.start();
        }

    }


    //消息持久化
    public void handleMessagePersist(List<MessageRecordDto> recordDtos) {

        List<PersistMessageRecordCommand> dtos = new ArrayList<>();

        recordDtos.forEach(r -> {
            PersistMessageRecordCommand cmd = new PersistMessageRecordCommand();
            cmd.setMessageRecordDto(r.toString());
            dtos.add(cmd);
        });

        Map<String, String> params = new HashMap<>();
//        params.put("messageRecordDto", dtos.toString());
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


    public void setQueue(ConcurrentLinkedQueue<MessageRecordDto> queue) {
        this.queue = queue;
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
                        if (dtos.size() > threshold || System.currentTimeMillis() > tick + 10 * 1000) { //当取出的条数大于99或者距离上次持久化过去10S
                            tick = System.currentTimeMillis();
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
