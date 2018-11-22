package com.everhomes.visitorsys.jsst;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.util.StringHelper;

import java.util.List;

public class JsstRequestParams {

    private String serviceId;
    private String requestType;
    private JSONObject attributes;
    private List<JSONObject> dataItems;

    public JsstRequestParams() {
        requestType = "DATA";
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public JSONObject getAttributes() {
        return attributes;
    }

    public void setAttributes(JSONObject attributes) {
        this.attributes = attributes;
    }

    public List<JSONObject> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<JSONObject> dataItems) {
        this.dataItems = dataItems;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
