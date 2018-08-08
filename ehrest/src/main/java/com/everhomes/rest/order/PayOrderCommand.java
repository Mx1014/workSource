package com.everhomes.rest.order;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class PayOrderCommand {

    @NotNull
    private Integer paymentType;
    
    @ItemType(String.class)
    private Map<String, String> paymentParams;
    
    @NotNull
    private String orderCommitToken;
    
    @NotNull
    private String orderCommitNonce;
    
    @NotNull
    private Long orderCommitTimestamp;
    
    @NotNull
    private String commitSignature;
    
    public PayOrderCommand() {
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Map<String, String> getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(Map<String, String> paymentParams) {
        this.paymentParams = paymentParams;
    }

    public String getOrderCommitToken() {
        return orderCommitToken;
    }

    public void setOrderCommitToken(String orderCommitToken) {
        this.orderCommitToken = orderCommitToken;
    }

    public String getOrderCommitNonce() {
        return orderCommitNonce;
    }

    public void setOrderCommitNonce(String orderCommitNonce) {
        this.orderCommitNonce = orderCommitNonce;
    }

    public Long getOrderCommitTimestamp() {
        return orderCommitTimestamp;
    }

    public void setOrderCommitTimestamp(Long orderCommitTimestamp) {
        this.orderCommitTimestamp = orderCommitTimestamp;
    }

    public String getCommitSignature() {
        return commitSignature;
    }

    public void setCommitSignature(String commitSignature) {
        this.commitSignature = commitSignature;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
