package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

public class PayOrderCommandResponse {
    private Integer paymentType;
    private String payInfo;

    public PayOrderCommandResponse() {
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
