//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/15.
 */

public class ShowBillForClientZJGKENTITY {
    private String version;
    private String errorScope;
    private String errorCode;
    private String errorDescription;
    private String errorDetails;
    private ShowBillForClientZJGKResponse response;

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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
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

    public ShowBillForClientZJGKResponse getResponse() {
        return response;
    }

    public void setResponse(ShowBillForClientZJGKResponse response) {
        this.response = response;
    }
}
