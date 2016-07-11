// @formatter:off
package com.everhomes.queue;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class DispatchQueueCommandResponsePdu implements Serializable {
    private static final long serialVersionUID = 1227938091543424321L;

    private Object executionResponse;
    private RuntimeException executionException;
    
    public DispatchQueueCommandResponsePdu() {
    }
    
    public DispatchQueueCommandResponsePdu(Object executionResponse) {
        this.executionResponse = executionResponse;
    }
    
    public DispatchQueueCommandResponsePdu(RuntimeException executionException) {
        this.executionException = executionException;
    }

    public Object getExecutionResponse() {
        return executionResponse;
    }

    public void setExecutionResponse(Object executionResponse) {
        this.executionResponse = executionResponse;
    }

    public RuntimeException getExecutionException() {
        return executionException;
    }

    public void setExecutionException(RuntimeException executionException) {
        this.executionException = executionException;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
