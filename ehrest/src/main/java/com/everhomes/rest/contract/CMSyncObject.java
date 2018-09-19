package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.util.List;

public class CMSyncObject {
    private String errorcode;

    private String total;

    private String currentpage;

    private String totalpage;

    private List<CMDataObject> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(String currentpage) {
        this.currentpage = currentpage;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public String getErrorCode() {
        return errorcode;
    }

    public void setErrorCode(String errorcode) {
        this.errorcode = errorcode;
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
