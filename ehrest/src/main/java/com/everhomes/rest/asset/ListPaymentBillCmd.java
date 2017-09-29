//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.Date;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class ListPaymentBillCmd {
    private Long nextPageAnchor;
    private Long pageSize;

    private String startPayTime;
    private String endPayTime;

    //交易时间
    private String payTime;
    //流水号？
    private String orderNo;
    //订单编号
    private String paymentOrderNum;
    //支付类型
    private Integer paymentType;
    //交易类型
    private Integer transactionType;
    @ItemType(Integer.class)
    private List<Integer> transactionTypes;
    private Integer paymentStatus;
    //结算状态
    private Integer settlementStatus;
    private Boolean distributionRemarkIsNull;
    //账单类型
    private String orderType;

    private String userType;

    private Long userId;
    @ItemType(ReSortCmd.class)
    private List<ReSortCmd> sorts;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(String startPayTime) {
        this.startPayTime = startPayTime;
    }

    public String getEndPayTime() {
        return endPayTime;
    }

    public void setEndPayTime(String endPayTime) {
        this.endPayTime = endPayTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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
