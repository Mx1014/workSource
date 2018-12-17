package com.everhomes.visitorsys.jsst;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.util.StringHelper;

import java.util.List;

public class JsstEntity {

    private String serviceId;
    private Integer resultCode;
    private String message;
    private JSONObject attributes;
    private List<JSONObject> dataItems;
    private String objectId;
    private String operateType;
    private List<JSONObject> subItems;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public List<JSONObject> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<JSONObject> subItems) {
        this.subItems = subItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
