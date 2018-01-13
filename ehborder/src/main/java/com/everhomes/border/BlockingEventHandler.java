package com.everhomes.border;

import com.everhomes.bus.*;
import com.everhomes.rest.messaging.BlockingEventResponse;
import com.everhomes.rest.messaging.BlockingEventStatus;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BlockingEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingEventHandler.class);

    static final Integer MAX_TIME_OUT =30*60*1000;

    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;

    @Autowired
    private LocalBusProvider localBusProvider;

    @Autowired
    private LocalBusTaskScheduler scheduler;

    @Autowired
    private LocalBus localBus;

    public BlockingEventResponse blockingEvent(String subjectId, String type, Integer timeOut) {
        if(timeOut == 0 || timeOut > MAX_TIME_OUT){
            return new BlockingEventResponse(BlockingEventStatus.ERROR,"timeOut over limit");
        }
        String subject = "blockingEventKey." + subjectId;
        BlockingEventResponse response = BlockingEventResponse.build(subject);

        switch (type){
            case "ONESHOT":
                localBusSubscriberBuilder.build(subject, new LocalBusOneshotSubscriber() {
                    @Override
                    public LocalBusSubscriber.Action onLocalBusMessage(Object sender, String subject, Object dtoResp, String path) {
                        response.setStatus(BlockingEventStatus.CONTINUTE);
                        response.incrCalledTime();
                        return null;
                    }
                    @Override
                    public void onLocalBusListeningTimeout() {
                        //wait again
                        response.setStatus(BlockingEventStatus.TIMEOUT);
                    }

                }).setTimeout(timeOut).create();

                break;
            case "ORORDINARY":
                LocalBusSubscriber subscriber = null;
                if((LocalBusSubscriber)BlockingEventResponse.BlockingEventStored.get(subject+"subscriber") != null){
                    subscriber = (LocalBusSubscriber)BlockingEventResponse.BlockingEventStored.get(subject+"subscriber");
                }else{
                    subscriber = new LocalBusSubscriber(){
                        @Override
                        public Action onLocalBusMessage(Object o, String s, Object o1, String s1) {
                            response.setStatus(BlockingEventStatus.CONTINUTE);
                            return null;
                        }
                    };

                    //把subscriber存起来
                    BlockingEventResponse.BlockingEventStored.set(subject+"subscriber",subscriber);
                }


                this.localBusProvider.subscribe(subject, subscriber);
                if (timeOut != null) {
                    Date startTime = new Date(DateHelper.currentGMTTime().getTime() + timeOut.longValue());
                    this.scheduler.getScheduler().schedule(new Runnable() {
                        public void run() {
                            localBusProvider.unsubscribe(subject, (LocalBusSubscriber)BlockingEventResponse.BlockingEventStored.get(subject+"subscriber"));
                        }
                    }, startTime);
                }
                break;
        }

        //dto 只有一个线程能成功，如果某一个线程失败，则需要重新请求 uuid
        return response;
    }


    public String signalBlockingEvent(String subjectId, String message) {
        String subject = "blockingEventKey." + subjectId;
        try {
            LocalBusSubscriber localBusSubscriber = new LocalBusSubscriber() {
                @Override
                public Action onLocalBusMessage(Object o, String s, Object o1, String s1) {
                    return null;
                }
            };
            localBusSubscriber.onLocalBusMessage(null, subject, StringHelper.toJsonString(message), null);
        } catch (Exception e) {
            LOGGER.error("submit LocalBusSubscriber failed, subject=" + subject, e);
        }

        try {
            localBus.publish(null, subject, StringHelper.toJsonString(message));
        } catch (Exception e) {
            LOGGER.error("submit localBus failed, subject=" + subject, e);
        }

        return "ok";
    }

}
