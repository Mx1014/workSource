package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>paymentSceneAppId: 支付场景id</li>
 * <li>paymentSceneAppName: 支付场景名称</li>
 * <li>orderNo: 订单号</li>
 * <li>payAmount: 订单金额</li>
 * <li>payTime: 支付时间</li>
 * </ul>
 */
public class EmployeePaymentLogDTO {
    private Long paymentSceneAppId;
    private String paymentSceneAppName;
    private String orderNo;
    private BigDecimal payAmount;
    private Long payTime;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
