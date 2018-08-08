package com.everhomes.rest.order;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class QueryOrderPaymentStatusCommand {
    @NotNull
    private String orderCommitToken;
    
    @NotNull
    private String orderCommitNonce;
    
    @NotNull
    private Long orderCommitTimestamp;
    
    @NotNull
    private String querySignature;
    
    public QueryOrderPaymentStatusCommand() {
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

    public String getQuerySignature() {
        return querySignature;
    }

    public void setQuerySignature(String signature) {
        this.querySignature = signature;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
