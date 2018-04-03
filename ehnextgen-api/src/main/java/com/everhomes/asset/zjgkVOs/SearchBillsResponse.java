//@formatter:off
package com.everhomes.asset.zjgkVOs;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/5.
 */

public class SearchBillsResponse {
    private String nextPageOffset;
    private String version;
    private String errorScope;
    private Integer errorCode;
    private String errorDescription;
    private String errorDetails;
    private List<SearchEnterpriseBillsDTO> response;

    public String getErrorScope() {
        return errorScope;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(String nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<SearchEnterpriseBillsDTO> getResponse() {
        return response;
    }

    public void setResponse(List<SearchEnterpriseBillsDTO> response) {
        this.response = response;
    }
}
