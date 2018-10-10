package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>orderNo: 订单号  </li>
 * <li>contactName: 姓名</li>
 * <li>contactToken: 手机号</li>
 * <li>paymentSceneAppName: 支付场景名</li>
 * <li>payTime: 支付时间</li>
 * <li>payAmount: 支付金额</li>
 * </ul>
 */
public class PaymentPayLogDTO {
    private String orderNo;
    private Long userId;
    private Long detailId;
    private String contactName;
    private String contactToken;
    private Long paymentSceneAppId;
    private String paymentSceneAppName;
    private Long payTime;
    private BigDecimal payAmount;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Long getPaymentSceneAppId() {
        return paymentSceneAppId;
    }

    public void setPaymentSceneAppId(Long paymentSceneAppId) {
        this.paymentSceneAppId = paymentSceneAppId;
    }

    public String getPaymentSceneAppName() {
        return paymentSceneAppName;
    }

    public void setPaymentSceneAppName(String paymentSceneAppName) {
        this.paymentSceneAppName = paymentSceneAppName;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
