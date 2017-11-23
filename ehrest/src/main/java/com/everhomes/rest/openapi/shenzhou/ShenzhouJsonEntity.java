package com.everhomes.rest.openapi.shenzhou;

import com.everhomes.rest.RestVersion;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/8.
 */
public class ShenzhouJsonEntity<T> {
    private String version;

    private String errorScope;

    private Integer errorCode;

    private String errorDescription;

    private String errorDetails;

    private Integer nextPageOffset;

    private T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
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

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getErrorScope() {
        return errorScope;
    }

    public void setErrorScope(String errorScope) {
        this.errorScope = errorScope;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
