// @formatter:off
package com.everhomes.parking.qididaoding;

import java.math.BigDecimal;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:51
 */
public class QidiDaodingLockCarInfo {
    private String parkingId;
    private String lockCarTime;
    private Integer status;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getLockCarTime() {
        return lockCarTime;
    }

    public void setLockCarTime(String lockCarTime) {
        this.lockCarTime = lockCarTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
