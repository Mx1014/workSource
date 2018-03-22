package com.everhomes.parking.vip_parking;

/**
 * @author sw on 2018/1/19.
 */
public class DingDingResponseEntity {
    private String OperTime;
    private String hubMac;
    private String operation;
    private String lockMac;
    private String BLEComm;
    private String receiveLockOperResult;
    private String LockOper;
    private String battery;
    private String LockStatus;
    private String CarStatus;
    private String connectResp;
    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getConnectResp() {
        return connectResp;
    }

    public void setConnectResp(String connectResp) {
        this.connectResp = connectResp;
    }

    public String getOperTime() {
        return OperTime;
    }

    public void setOperTime(String operTime) {
        OperTime = operTime;
    }

    public String getHubMac() {
        return hubMac;
    }

    public void setHubMac(String hubMac) {
        this.hubMac = hubMac;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getLockMac() {
        return lockMac;
    }

    public void setLockMac(String lockMac) {
        this.lockMac = lockMac;
    }

    public String getBLEComm() {
        return BLEComm;
    }

    public void setBLEComm(String BLEComm) {
        this.BLEComm = BLEComm;
    }

    public String getReceiveLockOperResult() {
        return receiveLockOperResult;
    }

    public void setReceiveLockOperResult(String receiveLockOperResult) {
        this.receiveLockOperResult = receiveLockOperResult;
    }

    public String getLockOper() {
        return LockOper;
    }

    public void setLockOper(String lockOper) {
        LockOper = lockOper;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getLockStatus() {
        return LockStatus;
    }

    public void setLockStatus(String lockStatus) {
        LockStatus = lockStatus;
    }

    public String getCarStatus() {
        return CarStatus;
    }

    public void setCarStatus(String carStatus) {
        CarStatus = carStatus;
    }
}
