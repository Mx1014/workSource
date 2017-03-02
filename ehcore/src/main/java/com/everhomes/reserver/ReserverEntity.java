package com.everhomes.reserver;

/**
 * Created by sw on 2017/3/2.
 */
public class ReserverEntity<T> {
    private Boolean result;
    private Object body;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
