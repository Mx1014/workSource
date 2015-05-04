// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>areaId: 区域Id</li>
 * <li>name: 小区名称</li>
 * <li>address: 小区地址</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * </ul>
 */
public class SuggestCommunityCommand {
    @NotNull
    private Long cityId;
    
    @NotNull
    private Long areaId;
    
    @NotNull
    private String name;
    
    private String address;
    private Double longitude;
    private Double latitude;
    
    private SuggestCommunityCommand() {
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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
