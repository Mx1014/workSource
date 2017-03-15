package com.everhomes.rest.reserver;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/3/1.
 */
public class CreateReserverOrderCommand {

    private Long id;
    private String orderId;
    private Long reserverTime;
    private Integer reserverNum;
    private String remark;
    private String requestorName;
    private String shopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getReserverTime() {
        return reserverTime;
    }

    public void setReserverTime(Long reserverTime) {
        this.reserverTime = reserverTime;
    }

    public Integer getReserverNum() {
        return reserverNum;
    }

    public void setReserverNum(Integer reserverNum) {
        this.reserverNum = reserverNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
