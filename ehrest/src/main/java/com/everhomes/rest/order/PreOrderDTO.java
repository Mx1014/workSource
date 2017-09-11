//@formatter:off
package com.everhomes.rest.order;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>expiredIntervalTime: 订单失效间隔时间，单位为秒</li>
 *     <li>amount: 支付金额，以分为单位</li>
 *     <li>orderCommitUrl: 付系统createOrder返回参数</li>
 *     <li>orderCommitToken: 支付系统createOrder返回参数</li>
 *     <li>orderCommitNonce: 支付系统createOrder返回参数</li>
 *     <li>orderCommitTimestamp: 支付系统createOrder返回参数</li>
 *     <li>payInfo: 支付信息</li>
 *     <li>extendInfo: 扩展信息</li>
 *     <li>payMethod: 支付方式 {@link com.everhomes.rest.order.PayMethodDTO}</li>
 * </ul>
 */
public class PreOrderDTO {

    private Long expiredIntervalTime;
    private Long amount;
    private String orderCommitUrl;
    private String orderCommitToken;
    private String orderCommitNonce;
    private Long orderCommitTimestamp;
    private String payInfo;
    private String extendInfo;
    @ItemType(PayMethodDTO.class)
    private List<PayMethodDTO> payMethod;

    public PreOrderDTO() {
    }

    public Long getExpiredIntervalTime() {
        return expiredIntervalTime;
    }

    public void setExpiredIntervalTime(Long expiredIntervalTime) {
        this.expiredIntervalTime = expiredIntervalTime;
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

    public Long getOrderCommitTimestamp() {
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
