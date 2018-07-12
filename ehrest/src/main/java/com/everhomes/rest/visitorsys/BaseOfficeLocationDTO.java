// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: (必填)办公地点ID</li>
 * <li>officeLocationName: (必填)名称</li>
 * <li>addresses: (必填)地点</li>
 * <li>longitude: (必填)精度</li>
 * <li>latitude: (必填)纬度</li>
 * <li>geohash: (必填)经纬度哈希值</li>
 * <li>mapAddresses: (必填)地图选点名称</li>
 * </ul>
 */
public class BaseOfficeLocationDTO {
    private Long id;
    private String officeLocationName;
    private String addresses;
    private Double longitude;
    private Double latitude;
    private String geohash;
    private String mapAddresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfficeLocationName() {
        return officeLocationName;
    }

    public void setOfficeLocationName(String officeLocationName) {
        this.officeLocationName = officeLocationName;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getMapAddresses() {
        return mapAddresses;
    }

    public void setMapAddresses(String mapAddresses) {
        this.mapAddresses = mapAddresses;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
