package com.everhomes.rest;

public class RestResponseBase {
    protected String version;
    protected String errorScope;
    protected Integer errorCode;
    protected String errorDescription;

    public RestResponseBase() {
        version = RestVersion.current().toString();
    }
    
    public RestResponseBase(String errorScope, int errorCode, String errorDescription) {
        this.version = RestVersion.current().toString();
        this.errorScope = errorScope;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getErrorScope() {
        return errorScope;
    }

    public void setErrorScope(String errorScope) {
        this.errorScope = errorScope;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
