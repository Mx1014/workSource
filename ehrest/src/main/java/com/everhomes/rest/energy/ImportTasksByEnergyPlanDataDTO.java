package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2017/11/23.
 */
public class ImportTasksByEnergyPlanDataDTO {
    private String id = "";
    private String meterNumber = "";
    private String meterName = "";
    private String meterType = "";
    private String executiveStartTime = "";
    private String executiveExpireTime = "";
    private String status = "";
    private String lastTaskReading = "";
    private String reading = "";

    public String getExecutiveExpireTime() {
        return executiveExpireTime;
    }

    public void setExecutiveExpireTime(String executiveExpireTime) {
        this.executiveExpireTime = executiveExpireTime;
    }

    public String getExecutiveStartTime() {
        return executiveStartTime;
    }

    public void setExecutiveStartTime(String executiveStartTime) {
        this.executiveStartTime = executiveStartTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastTaskReading() {
        return lastTaskReading;
    }

    public void setLastTaskReading(String lastTaskReading) {
        this.lastTaskReading = lastTaskReading;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
