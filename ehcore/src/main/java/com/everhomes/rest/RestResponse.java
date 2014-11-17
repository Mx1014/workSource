package com.everhomes.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * To unify all REST responses in a standard way
 * 
 * @author Kelven Yang
 *
 */
@JsonInclude(Include.NON_NULL)
public class RestResponse {
    private String version;
    private String errorScope;
    private Integer errorCode;
    private String errorDescription;
    
    @JsonProperty("response")
    private Object responseObject;
    
    public RestResponse() {
        version = RestVersion.current().toString();
    }
    
    public RestResponse(String errorScope, int errorCode, String errorDescription) {
        version = RestVersion.current().toString();
        this.errorScope = errorScope;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
    
    public RestResponse(Object responseObject) {
        version = RestVersion.current().toString();
        this.responseObject = responseObject;
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

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
