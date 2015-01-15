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
public class RestResponse extends RestResponseBase {
    
    @JsonProperty("response")
    private Object responseObject;
    
    public RestResponse() {
        version = RestVersion.current().toString();
    }
    
    public RestResponse(String errorScope, int errorCode, String errorDescription) {
        super(errorScope, errorCode, errorDescription);
    }
    
    public RestResponse(Object responseObject) {
        super();
        this.responseObject = responseObject;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
