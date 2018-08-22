package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.util.List;

public class CMSyncObject {
    private String errorCode;

    private List<CMDataObject> data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<CMDataObject> getData() {
        return data;
    }

    public void setData(List<CMDataObject> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
