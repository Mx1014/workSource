package com.everhomes.util;

public class FrequencyControlException extends Exception{
    private static final long serialVersionUID = 0;

    public FrequencyControlException() {
        super("HTTP请求超出设定的限制");
    }

    public FrequencyControlException(String message) {
        super(message);
    }
}
