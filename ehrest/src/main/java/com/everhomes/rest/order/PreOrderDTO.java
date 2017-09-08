//@formatter:off
package com.everhomes.rest.order;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public class PreOrderDTO {

    private Long amount;
    private String orderCommitUrl;
    private String orderCommitToken;
    private String orderCommitNonce;
    private Long orderCommitTimestamp;
    private String payInfo;
    private String extendInfo;
    private List<PayMethodDTO> payMethod;

    public PreOrderDTO() {
    }

    public Long getAmount() {

        return amount;
    }

    public void setAmount(Long amount) {
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

    public Long  getOrderCommitTimestamp() {
        return orderCommitTimestamp;
    }

    public void setOrderCommitTimestamp(Long orderCommitTimestamp) {
        this.orderCommitTimestamp = orderCommitTimestamp;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public List<PayMethodDTO> getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(List<PayMethodDTO> payMethod) {
        this.payMethod = payMethod;
    }
}
