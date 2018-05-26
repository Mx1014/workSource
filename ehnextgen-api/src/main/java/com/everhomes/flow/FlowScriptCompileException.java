package com.everhomes.flow;

/**
 * 编译错误异常
 */
public class FlowScriptCompileException extends RuntimeException {
    public FlowScriptCompileException() {
        super();
    }

    public FlowScriptCompileException(String message) {
        super(message);
    }

    public FlowScriptCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowScriptCompileException(Throwable cause) {
        super(cause);
    }

    protected FlowScriptCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
