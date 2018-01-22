package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class RentalOrderActionData implements Serializable{
    private static final long serialVersionUID = 7502654058025166257L;

    private Long orderId;
    private String resourceType;
    private Long metaObject;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getMetaObject() {
        return metaObject;
    }

    public void setMetaObject(Long metaObject) {
        this.metaObject = metaObject;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
