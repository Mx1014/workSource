// @formatter:off
package com.everhomes.rest;

public class RestResponseBase {
    protected String version;
    protected String errorScope;
    protected Integer errorCode;
    protected String errorDescription;
    protected String errorDetails;

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
    
    /**
     * To work around default Jackson JSON serialization behaviour (it automatically detects getters and treat them as properties)
     * although we can add Jackson annotation to disable the default Jackson JSON behaviour, but it will introduce Jackson JSON dependency
     *
     * Therefore we have isSuccess take the shape of being a static method here
     */
    public static boolean isSuccess(RestResponseBase response) {
        return RestErrorCode.SCOPE_GENERAL.equalsIgnoreCase(response.errorScope) && (RestErrorCode.SUCCESS == response.errorCode.intValue()
            || RestErrorCode.SUCCESS_MORE_DATA == response.errorCode.intValue());
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

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
