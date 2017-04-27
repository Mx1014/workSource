package com.everhomes.rest.organization;

/**
 * Created by sfyan on 2017/4/19.
 */
public class ImportFileResultLog<T> {

    private T data;

    private String log;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
