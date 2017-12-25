package com.everhomes.reserver;

/**
 * Created by sw on 2017/3/2.
 */
public class ReserverEntity<T> {
    private Boolean result;
    private T body;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
