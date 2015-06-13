// @formatter:off
package com.everhomes.community;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>communityId: 被更新的小区Id</li>
 * <li>address: 小区地址</li>
 * <li>cityId: 小区所在城市IdId</li>
 * <li>areaId：小区所在城市区县Id</li>
 * <li>longitude: 小区经度</li>
 * <li>longitude: 小区纬度</li>
 * </ul>
 */
public class UpdateCommunityCommand {
    @NotNull
    private Long communityId;
    
    private String address;
    @NotNull
    private Long cityId;
    @NotNull
    private Long areaId;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
    
    public UpdateCommunityCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
