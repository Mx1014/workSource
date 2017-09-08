//@formatter:off
package com.everhomes.asset.zjgkVOs;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class ContractDetailResponse {
    private String errorScope;
    private Integer errorCode;
    private String errorDescription;
    private String errorDetails;
    private ContractDTO response;

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

    public ContractDTO getResponse() {
        return response;
    }

    public void setResponse(ContractDTO response) {
        this.response = response;
    }
}
