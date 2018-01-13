package com.everhomes.rest.messaging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.everhomes.rest.messaging.BlockingEventStatus.BLOCKING;

public class BlockingEventResponse {
    public BlockingEventResponse(BlockingEventStatus status, String errorMessage) {
        this.setStatus(status);
        this.setErrorMessage(errorMessage);
    }

    private BlockingEventStatus status;
    private String errorMessage;
    private Integer calledTimes = 0;
    private String subject;
    private transient static ConcurrentHashMap blockingEventStored;

    public BlockingEventStatus getStatus() {
        return status;
    }

    public void setStatus(BlockingEventStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getCalledTimes() {
        return calledTimes;
    }

    public void setCalledTimes(Integer calledTimes) {
        this.calledTimes = calledTimes;
    }

    public static BlockingEventResponse build(ConcurrentHashMap stored,String subject) {
        BlockingEventResponse.blockingEventStored = stored;
        BlockingEventResponse response = new BlockingEventResponse(BlockingEventStatus.BLOCKING,null);
        if(blockingEventStored.get(subject + ".calledTimes") == null){
            blockingEventStored.put(subject + ".calledTimes", 0);
            response.setCalledTimes(0);
        }else{
            response.setCalledTimes(Integer.valueOf(blockingEventStored.get(subject+ ".calledTimes").toString()));
        }
        return response;
    }

    public void incrCalledTime(){
        Object value = blockingEventStored.get(subject + ".calledTimes");
        if(value == null){
            blockingEventStored.put(subject + ".calledTimes", 0);
            setCalledTimes(0);
        }else{
            value = (Integer) value + 1;
            blockingEventStored.put(subject + ".calledTimes", value);
            setCalledTimes((Integer) value);
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
