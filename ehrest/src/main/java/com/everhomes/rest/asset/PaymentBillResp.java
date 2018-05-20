//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

/**
 * @author change by yangcx
 * @date 2018年5月19日----下午8:18:48
 */
/**
 *<ul>
 * <li>userId:用户ID</li>
 * <li>orderNo: 支付流水号，如：WUF00000000000001098</li> 
 * <li>paymentOrderNum:订单编号，如：954650447962984448</li>
 * <li>payTime:交易时间</li>
 * <li>paymentType:支付方式，微信/支付宝/对公转账</li>
 * <li>transactionType:交易类型，如：手续费/充值/提现/退款等</li>
 * <li>orderAmount:交易金额</li>
 * <li>amount:入账金额</li>
 * <li>feeAmount:手续费</li>
 * <li>paymentStatus:支付状态: 0未支付,1支付成功,2挂起,3失败
 * 		转换为业务系统的订单状态：已完成/订单异常</li>
 * <li>settlementStatus:结算状态：已结算/待结算</li> 
 * <li>orderRemark1:业务系统自定义字段（要求传订单来源）：wuyeCode</li>
 * <li>orderRemark2:</li>
 * <li>paymentOrderId:</li>
 * <li>payerName[String]:缴费人</li>
 * <li>payerTel[String]:缴费人电话</li>	
 *</ul>
 */
public class PaymentBillResp {
    private Long userId;
    private String orderNo;
    private String paymentOrderNum;
    private String payTime;
    private Integer paymentType;
    private Integer transactionType;
    private BigDecimal orderAmount;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private Integer paymentStatus;
    private Integer settlementStatus;
    private String orderRemark1;
    private Long paymentOrderId;

    private String orderRemark2;
    private String orderSource;
    private String payerName;
    private String payerTel;

    public String getPayerTel() {
        return payerTel;
    }

    public void setPayerTel(String payerTel) {
        this.payerTel = payerTel;
    }

    public String getOrderRemark2() {
        return orderRemark2;
    }

    public void setOrderRemark2(String orderRemark2) {
        this.orderRemark2 = orderRemark2;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
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
