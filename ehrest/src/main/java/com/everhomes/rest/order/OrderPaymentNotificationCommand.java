package com.everhomes.rest.order;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/19.
 */
public class OrderPaymentNotificationCommand {
    private Long orderId;
    private Integer orderType;
    private String bizOrderNum;
    private Integer paymentType;
    private Map<String, String> paymentParams;
    private Long payerUserId;
    private Long amount;
    private Long remainAmount;
    private Integer refundDestination;
    private String payDatetime;
    private String extendInfo;
    private Integer paymentStatus;
    private String paymentErrorCode;
    private String paymentMessage;
    private String signature;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getBizOrderNum() {
        return bizOrderNum;
    }

    public void setBizOrderNum(String bizOrderNum) {
        this.bizOrderNum = bizOrderNum;
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

    public Long getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(Long payerUserId) {
        this.payerUserId = payerUserId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Long remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Integer getRefundDestination() {
        return refundDestination;
    }

    public void setRefundDestination(Integer refundDestination) {
        this.refundDestination = refundDestination;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentErrorCode() {
        return paymentErrorCode;
    }

    public void setPaymentErrorCode(String paymentErrorCode) {
        this.paymentErrorCode = paymentErrorCode;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
