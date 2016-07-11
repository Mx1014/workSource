package com.everhomes.aclink.lingling;

import java.util.Map;

import com.everhomes.util.StringHelper;

public class AclinkLinglingMapResponse {
    private Map<String, String> responseResult;
    private String methodName;
    private String statusCode;
    
    public Map<String, String> getResponseResult() {
        return responseResult;
    }
    public void setResponseResult(Map<String, String> responseResult) {
        this.responseResult = responseResult;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
