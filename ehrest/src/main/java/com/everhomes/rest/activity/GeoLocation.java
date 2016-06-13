// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <p>经纬度位置</p>
 * <ul>
 * <li>latitude: 纬度</li>
 * <li>longitude: 经度</li>
 * </ul>
 */
public class GeoLocation {
    private Double latitude;
    private Double longitude;

    public GeoLocation() {
    }
    
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
