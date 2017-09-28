//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.Date;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class ListPaymentBillCmd {
    private Long offset;
    private Long limit;

    private Date startPayTime;
    private Date endPayTime;
    private String orderNo;
    private String paymentOrderNum;
    private Integer paymentType;
    private Integer transactionType;
    @ItemType(Integer.class)
    private List<Integer> transactionTypes;
    private Integer paymentStatus;
    private Integer settlementStatus;
    private Boolean distributionRemarkIsNull;

    private String userType;

    private Long userId;
    @ItemType(ReSortCmd.class)
    private List<ReSortCmd> sorts;

    public Long getOffset() {
        return offset;
    }
    public void setOffset(Long offset) {
        this.offset = offset;
    }
    public Long getLimit() {
        return limit;
    }
    public void setLimit(Long limit) {
        this.limit = limit;
    }
    public Date getStartPayTime() {
        return startPayTime;
    }
    public void setStartPayTime(Date startPayTime) {
        this.startPayTime = startPayTime;
    }
    public Date getEndPayTime() {
        return endPayTime;
    }
    public void setEndPayTime(Date endPayTime) {
        this.endPayTime = endPayTime;
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
    public List<Integer> getTransactionTypes() {
        return transactionTypes;
    }
    public void setTransactionTypes(List<Integer> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }
    public List<ReSortCmd> getSorts() {
        return sorts;
    }
    public void setSorts(List<ReSortCmd> sorts) {
        this.sorts = sorts;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getDistributionRemarkIsNull() {
        return distributionRemarkIsNull;
    }
    public void setDistributionRemarkIsNull(Boolean distributionRemarkIsNull) {
        this.distributionRemarkIsNull = distributionRemarkIsNull;
    }
}
