package com.everhomes.aclink.lingling;

import com.everhomes.util.StringHelper;

public class AclinkLinglingQRCodeKeyResponse {
    private AclinkLinglingQRCode responseResult;
    private String methodName;
    private String statusCode;
    
    public AclinkLinglingQRCode getResponseResult() {
        return responseResult;
    }
    public void setResponseResult(AclinkLinglingQRCode responseResult) {
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
