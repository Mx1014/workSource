package com.everhomes.util;

import com.everhomes.message.MessageRecord;
import com.everhomes.message.MessageService;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagePersistType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.statistics.event.StatEventDeviceLogProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessagePersistWorker implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePersistWorker.class);

    //    @Value("${core.service.uri}")
    // private String coreServiceUri = "http://10.1.10.37:8080/evh";

    //    @Value("${border.app.key}")
    // private String appKey = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";

    //    @Value("${border.app.secret}")
    // private String secretKey = "2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ";

    private static volatile BlockingQueue<MessageRecordDto> queue = new LinkedBlockingQueue<>();

    public static BlockingQueue<MessageRecordDto> getQueue() {
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

    // @Autowired
    // private TaskScheduler taskScheduler;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StatEventDeviceLogProvider statEventDeviceLogProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180525
    // @PostConstruct
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

        // 每次满一百条就持久化一次
        Worker worker = new Worker(100);
        worker.start();

    }

    //消息持久化
    private void handleMessagePersist(List<MessageRecordDto> recordDtos) {
        List<MessageRecord> records = new ArrayList<>();
        for (MessageRecordDto dto : recordDtos) {
            MessageRecord record = ConvertHelper.convert(dto, MessageRecord.class);

            //当存在自sessionToken时，使用sessionToken解析接收者
            if (StringUtils.isNotEmpty(dto.getSessionToken())) {
                LoginToken login = WebTokenGenerator.getInstance().fromWebToken(dto.getSessionToken(), LoginToken.class);
                if (login != null) {
                    record.setDstChannelToken(String.valueOf(login.getUserId()));
                    record.setDstChannelType(ChannelType.USER.getCode());
                }
            }

            //当deviceId存在时，使用deviceId解析接收者
            if (StringUtils.isNotEmpty(dto.getDeviceId())) {
                Long dstChannelToken = statEventDeviceLogProvider.findUidByDeviceId(dto.getDeviceId());
                if (dstChannelToken != null && dstChannelToken != 0L) {
                    record.setDstChannelToken(dstChannelToken.toString());
                    record.setDstChannelType(ChannelType.USER.getCode());
                }
            }

            MessagePersistType persistType = getMessagePersistType(dto.getMeta());
            switch (persistType) {
                case DB:
                    records.add(record);
                    break;
                case LOG:
                    LOGGER.info("{} {}", record, dto.getMeta());
                    break;
                case DISCARD:
                    break;
                default:
                    break;
            }
        }
        messageService.persistMessage(records);

        /*this.restCall("/message/persistMessage", params, new ListenableFutureCallback<ResponseEntity<String>>() {

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
        });*/
    }

    private MessagePersistType getMessagePersistType(Map<String, String> meta) {
        MessagePersistType persistType = null;
        if (meta != null) {
            String persist = meta.get(MessageMetaConstant.PERSIST_TYPE);
            if (persist != null) {
                try {
                    Byte persistByte = Byte.valueOf(persist);
                    persistType = MessagePersistType.fromCode(persistByte);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return persistType != null ? persistType : MessagePersistType.DB;
    }


    /*public void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) {
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
    }*/


    public void setQueue(BlockingQueue<MessageRecordDto> queue) {
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
            List<MessageRecordDto> dtos = new ArrayList<>();
            while (!this.finished) {
                try {
                    MessageRecordDto record = queue.take();
                    dtos.add(record);

                    if (dtos.size() > threshold - 1 || System.currentTimeMillis() > tick + 10 * 1000) { //当取出的条数大于99或者距离上次持久化过去10S
                        // dtos.add(record);
                        tick = System.currentTimeMillis();
                        handleMessagePersist(dtos);
                        dtos.clear();
                    } else if (this.finished) {
                        handleMessagePersist(dtos);
                        dtos.clear();
                        Thread.sleep(60 * 1000L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dtos.clear();
                }
            }
        }
    }
}
