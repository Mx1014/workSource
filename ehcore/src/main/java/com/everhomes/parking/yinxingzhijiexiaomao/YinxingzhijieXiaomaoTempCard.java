// @formatter:off
package com.everhomes.parking.yinxingzhijiexiaomao;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/3/28 15:01
 */
public class YinxingzhijieXiaomaoTempCard {
    private Integer flag;
    private String message;
    private String parkName;
    private String startTime;
    private String chargTime;
    private String totalPay;
    private String shouldPay;
    private String couponInfo;
    private String orderId;
    private Integer timeOut;

    public boolean isSuccess() {
        return flag!=null && flag==1;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getChargTime() {
        return chargTime;
    }

    public void setChargTime(String chargTime) {
        this.chargTime = chargTime;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(String totalPay) {
        this.totalPay = totalPay;
    }

    public String getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(String shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
