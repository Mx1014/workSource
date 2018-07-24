package com.everhomes.flow;

public class FlowFunctionException extends RuntimeException {
    public FlowFunctionException() {
        super();
    }

    public FlowFunctionException(String message) {
        super(message);
    }

    public FlowFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowFunctionException(Throwable cause) {
        super(cause);
    }

    protected FlowFunctionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
