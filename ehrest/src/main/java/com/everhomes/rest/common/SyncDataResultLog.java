package com.everhomes.rest.common;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public class SyncDataResultLog<T> {

    private T data;

    private String errorLog;

    private String scope;

    private Integer code;

    private String errorDescription;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SyncDataResultLog(String scope){
        this.scope = scope;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
