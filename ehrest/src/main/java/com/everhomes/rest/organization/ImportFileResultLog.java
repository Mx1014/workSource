package com.everhomes.rest.organization;

/**
 * Created by sfyan on 2017/4/19.
 */
public class ImportFileResultLog<T> {

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

    public ImportFileResultLog(String scope){
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
