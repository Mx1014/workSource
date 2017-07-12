package com.everhomes.parking.bosigao;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/10.
 */
public class Pkorder {
    private String RecordID;
    private String OrderID;
    //1: 临时卡缴费 2：月卡续费
    private Integer OrderType;
    private Integer PayWay;
    private BigDecimal Amount;
    private BigDecimal OutstandingAmount;
    private BigDecimal PayAmount;
    private Integer Status;
    private String OrderTime;
    private String OldUserulDate;
    private String NewUsefulDate;
    private String OnlineUserID;
    private String OnlineOrderID;

    public String getRecordID() {
        return RecordID;
    }

    public void setRecordID(String recordID) {
        RecordID = recordID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public Integer getOrderType() {
        return OrderType;
    }

    public void setOrderType(Integer orderType) {
        OrderType = orderType;
    }

    public Integer getPayWay() {
        return PayWay;
    }

    public void setPayWay(Integer payWay) {
        PayWay = payWay;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public BigDecimal getOutstandingAmount() {
        return OutstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        OutstandingAmount = outstandingAmount;
    }

    public BigDecimal getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        PayAmount = payAmount;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOldUserulDate() {
        return OldUserulDate;
    }

    public void setOldUserulDate(String oldUserulDate) {
        OldUserulDate = oldUserulDate;
    }

    public String getNewUsefulDate() {
        return NewUsefulDate;
    }

    public void setNewUsefulDate(String newUsefulDate) {
        NewUsefulDate = newUsefulDate;
    }

    public String getOnlineUserID() {
        return OnlineUserID;
    }

    public void setOnlineUserID(String onlineUserID) {
        OnlineUserID = onlineUserID;
    }

    public String getOnlineOrderID() {
        return OnlineOrderID;
    }

    public void setOnlineOrderID(String onlineOrderID) {
        OnlineOrderID = onlineOrderID;
    }
}
