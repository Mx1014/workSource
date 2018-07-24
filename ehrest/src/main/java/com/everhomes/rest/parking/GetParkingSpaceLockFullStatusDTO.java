// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>battery : 电量百分比</li>
  *<li>lockStatus : 当前状态 {@link com.everhomes.rest.parking.ParkingSpaceLockStatus}</li>
  *<li>carStatus : 是否有车,01有车辆，ff无车辆，02车辆有无判断中</li>
  *</ul>
  */

public class GetParkingSpaceLockFullStatusDTO {
    private String battery;
    private String lockStatus;
    private String carStatus;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }
}
