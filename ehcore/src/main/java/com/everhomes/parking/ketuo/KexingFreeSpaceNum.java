package com.everhomes.parking.ketuo;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/21.
 */
public class KexingFreeSpaceNum {
    private Integer parkId;
    private String parkName;
    private Integer totalNum;
    private Integer freeSpaceNum;

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

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getFreeSpaceNum() {
        return freeSpaceNum;
    }

    public void setFreeSpaceNum(Integer freeSpaceNum) {
        this.freeSpaceNum = freeSpaceNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
