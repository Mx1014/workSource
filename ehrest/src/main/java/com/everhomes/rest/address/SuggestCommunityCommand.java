// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>regionId: 区域Id(城市，区县等)</li>
 * <li>name: 小区名称</li>
 * <li>address: 小区地址</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * </ul>
 */
public class SuggestCommunityCommand {
    @NotNull
    private Long regionId;
    
    //private Long areaId;
    
    @NotNull
    private String name;
    
    private String address;
    private Double longitude;
    private Double latitude;
    
    public SuggestCommunityCommand() {
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
