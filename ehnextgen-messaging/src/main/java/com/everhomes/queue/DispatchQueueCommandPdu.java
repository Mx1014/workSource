// @formatter:off
package com.everhomes.queue;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class DispatchQueueCommandPdu implements Serializable {
    
    private static final long serialVersionUID = 8480535158257453278L;

    private String requestUuid;
    private String originationSequence;
    private DispatchableCommand command; 
    
    public DispatchQueueCommandPdu() {
    }
    
    public DispatchQueueCommandPdu(DispatchableCommand command, String originationSequence) {
        this.command = command;
        this.originationSequence = originationSequence;
    }
    
    public DispatchQueueCommandPdu(String requestUuid, DispatchableCommand command, String originationSequence) {
        this.requestUuid = requestUuid;
        this.command = command;
        this.originationSequence = originationSequence;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public DispatchableCommand getCommand() {
        return command;
    }

    public void setCommand(DispatchableCommand command) {
        this.command = command;
    }
    
    public String getOriginationSequence() {
        return this.originationSequence;
    }
    
    public void setOriginationSequence(String originationSequence) {
        this.originationSequence = originationSequence;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
