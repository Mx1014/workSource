// @formatter:off
package com.everhomes.parking.qididaoding;

import java.math.BigDecimal;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:51
 */
public class QidiDaodingTemporaryCarEntity {
    //临时车信息查询
    private String parkingId;
    private String parkingName;
    private String plateNo;
    private String plateColor;
    private BigDecimal paidAmt;
    private String entryTime;
    private Integer parkingTimes;
    private String exittime;
    private BigDecimal receivable;
    private Integer overLeavingTime;
    private String validLeavingTime;
    private BigDecimal lastPaid;
    private String lastPaidTime;
    private String calcId;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public BigDecimal getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getParkingTimes() {
        return parkingTimes;
    }

    public void setParkingTimes(Integer parkingTimes) {
        this.parkingTimes = parkingTimes;
    }

    public String getExittime() {
        return exittime;
    }

    public void setExittime(String exittime) {
        this.exittime = exittime;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public Integer getOverLeavingTime() {
        return overLeavingTime;
    }

    public void setOverLeavingTime(Integer overLeavingTime) {
        this.overLeavingTime = overLeavingTime;
    }

    public String getValidLeavingTime() {
        return validLeavingTime;
    }

    public void setValidLeavingTime(String validLeavingTime) {
        this.validLeavingTime = validLeavingTime;
    }

    public BigDecimal getLastPaid() {
        return lastPaid;
    }

    public void setLastPaid(BigDecimal lastPaid) {
        this.lastPaid = lastPaid;
    }

    public String getLastPaidTime() {
        return lastPaidTime;
    }

    public void setLastPaidTime(String lastPaidTime) {
        this.lastPaidTime = lastPaidTime;
    }

    public String getCalcId() {
        return calcId;
    }

    public void setCalcId(String calcId) {
        this.calcId = calcId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
