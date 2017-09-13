//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.rest.order.PayMethodDTO;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/12.
 */

public class PlaceAnAssetOrderResponse {
    private Long expiredIntervalTime;
    private String amount;
    private String orderCommitUrl;
    private String orderCommitToken;
    private String orderCommitNonce;
    private Long orderCommitTimestamp;
    private List<PayMethodDTO> payMethod;

    public Long getExpiredIntervalTime() {
        return expiredIntervalTime;
    }

    public void setExpiredIntervalTime(Long expiredIntervalTime) {
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

    public Long getOrderCommitTimestamp() {
        return orderCommitTimestamp;
    }

    public void setOrderCommitTimestamp(Long orderCommitTimestamp) {
        this.orderCommitTimestamp = orderCommitTimestamp;
    }

    public List<PayMethodDTO> getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(List<PayMethodDTO> payMethod) {
        this.payMethod = payMethod;
    }
}
