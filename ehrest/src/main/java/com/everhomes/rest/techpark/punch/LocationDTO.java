package com.everhomes.rest.techpark.punch;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>latitude: 坐标纬度</li>
 * <li>longitude： 坐标经度</li>
 * </ul>
 */
public class LocationDTO {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
