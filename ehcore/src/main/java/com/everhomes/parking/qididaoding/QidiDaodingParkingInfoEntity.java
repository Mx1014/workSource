// @formatter:off
package com.everhomes.parking.qididaoding;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:51
 */
public class QidiDaodingParkingInfoEntity {
    private Integer totalParks;
    private String parkingName;
    private Integer emptyParks;

    public Integer getTotalParks() {
        return totalParks;
    }

    public void setTotalParks(Integer totalParks) {
        this.totalParks = totalParks;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public Integer getEmptyParks() {
        return emptyParks;
    }

    public void setEmptyParks(Integer emptyParks) {
        this.emptyParks = emptyParks;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
