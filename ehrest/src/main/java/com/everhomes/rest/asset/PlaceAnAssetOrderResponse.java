//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/12.
 */

public class PlaceAnAssetOrderResponse {
    private String expiredIntervalTime;
    private String amount;
    private String orderCommitUrl;
    private String orderCommitToken;
    private String orderCommitNonce;
    private String orderCommitTimestamp;
    private List<PayMethod> payMethod;

    public String getExpiredIntervalTime() {
        return expiredIntervalTime;
    }

    public void setExpiredIntervalTime(String expiredIntervalTime) {
        this.expiredIntervalTime = expiredIntervalTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderCommitUrl() {
        return orderCommitUrl;
    }

    public void setOrderCommitUrl(String orderCommitUrl) {
        this.orderCommitUrl = orderCommitUrl;
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

    public String getOrderCommitTimestamp() {
        return orderCommitTimestamp;
    }

    public void setOrderCommitTimestamp(String orderCommitTimestamp) {
        this.orderCommitTimestamp = orderCommitTimestamp;
    }

    public List<PayMethod> getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(List<PayMethod> payMethod) {
        this.payMethod = payMethod;
    }
}
