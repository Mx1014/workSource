package com.everhomes.parking.dashi;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/22.
 */
public class DashiCarLocation {
    private String ParkingLength;
    private String ParkingPlace;
    private String ParkingStartTime;
    private String ParkingPhoto;
    private String CarNo;
    private String SpaceCode;

    public String getParkingStartTime() {
        return ParkingStartTime;
    }

    public void setParkingStartTime(String parkingStartTime) {
        ParkingStartTime = parkingStartTime;
    }

    public String getParkingLength() {
        return ParkingLength;
    }

    public void setParkingLength(String parkingLength) {
        ParkingLength = parkingLength;
    }

    public String getParkingPlace() {
        return ParkingPlace;
    }

    public void setParkingPlace(String parkingPlace) {
        ParkingPlace = parkingPlace;
    }

    public String getParkingPhoto() {
        return ParkingPhoto;
    }

    public void setParkingPhoto(String parkingPhoto) {
        ParkingPhoto = parkingPhoto;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getSpaceCode() {
        return SpaceCode;
    }

    public void setSpaceCode(String spaceCode) {
        SpaceCode = spaceCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
