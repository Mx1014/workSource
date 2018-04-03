package com.everhomes.parking.ketuo;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/21.
 */
public class KetuoParking {
    private Integer parkId;
    private String parkName;
    private Integer totalSpaceNum;

    public Integer getParkId() {
        return parkId;
    }

    public void setParkId(Integer parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public Integer getTotalSpaceNum() {
        return totalSpaceNum;
    }

    public void setTotalSpaceNum(Integer totalSpaceNum) {
        this.totalSpaceNum = totalSpaceNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
