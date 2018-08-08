package com.everhomes.parking.fujica;

import java.math.BigDecimal;

public class FujicaJsonParam {
    private String ParkingCode;
    private String CarNo;
    private String ParkingName;
    private String BeginTime;
    private String ChargeableTime;
    private BigDecimal ParkingFee;
    private BigDecimal ActualAmount;
    private Integer FreeTime;
    private Integer OverTime;

    public String getParkingCode() {
        return ParkingCode;
    }

    public void setParkingCode(String parkingCode) {
        ParkingCode = parkingCode;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getParkingName() {
        return ParkingName;
    }

    public void setParkingName(String parkingName) {
        ParkingName = parkingName;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getChargeableTime() {
        return ChargeableTime;
    }

    public void setChargeableTime(String chargeableTime) {
        ChargeableTime = chargeableTime;
    }

    public BigDecimal getParkingFee() {
        return ParkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        ParkingFee = parkingFee;
    }

    public BigDecimal getActualAmount() {
        return ActualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        ActualAmount = actualAmount;
    }

    public Integer getFreeTime() {
        return FreeTime;
    }

    public void setFreeTime(Integer freeTime) {
        FreeTime = freeTime;
    }

    public Integer getOverTime() {
        return OverTime;
    }

    public void setOverTime(Integer overTime) {
        OverTime = overTime;
    }
}
