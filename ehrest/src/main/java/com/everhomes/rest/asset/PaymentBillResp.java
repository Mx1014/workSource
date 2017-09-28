//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class PaymentBillResp {
    private Long userId;
    private String orderNo;
    private String paymentOrderNum;
    private Date payTime;
    private Integer paymentType;
    private Integer transactionType;
    private BigDecimal orderAmount;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private Integer paymentStatus;
    private Integer settlementStatus;
    private String orderRemark1;
    private Long paymentOrderId;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Date getPayTime() {
        return payTime;
    }
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }
    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }
    public Integer getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    public Integer getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }
    public Integer getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Integer getSettlementStatus() {
        return settlementStatus;
    }
    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
    public String getOrderRemark1() {
        return orderRemark1;
    }
    public void setOrderRemark1(String orderRemark1) {
        this.orderRemark1 = orderRemark1;
    }
    public Long getPaymentOrderId() {
        return paymentOrderId;
    }
    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }
}
