package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

public class QueryOrderPaymentStatusCommandResponse {
    private Integer payStatus;
    
    public QueryOrderPaymentStatusCommandResponse() {
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
