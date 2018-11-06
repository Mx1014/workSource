package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public class RAAccountObject {
    Integer errorCode;

    String errorDescription;

    String errorDetails;

    String AccountID;

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

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
