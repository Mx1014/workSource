//@formatter:off
package com.everhomes.asset.zjgkVOs;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class BillDetailResponse {
    private String version;
    private String errorScope;
    private Integer errorCode;
    private String errorDescription;
    private String errorDetails;
    private BillDetailDTO response;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public BillDetailDTO getResponse() {
        return response;
    }

    public void setResponse(BillDetailDTO response) {
        this.response = response;
    }
}
