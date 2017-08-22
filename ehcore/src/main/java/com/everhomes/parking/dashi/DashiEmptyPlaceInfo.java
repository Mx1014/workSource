package com.everhomes.parking.dashi;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/22.
 */
public class DashiEmptyPlaceInfo {

    private String ParkingName;
    private List<DashiEmptyPlaceFloorInfo> EmptyPlaceFloorInfo;

    public String getParkingName() {
        return ParkingName;
    }

    public void setParkingName(String parkingName) {
        ParkingName = parkingName;
    }

    public List<DashiEmptyPlaceFloorInfo> getEmptyPlaceFloorInfo() {
        return EmptyPlaceFloorInfo;
    }

    public void setEmptyPlaceFloorInfo(List<DashiEmptyPlaceFloorInfo> emptyPlaceFloorInfo) {
        EmptyPlaceFloorInfo = emptyPlaceFloorInfo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
