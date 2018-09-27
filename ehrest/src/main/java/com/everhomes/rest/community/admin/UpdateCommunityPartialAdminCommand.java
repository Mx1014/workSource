// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>communityId: 被更新的小区Id</li>
 *     <li>name: 项目名称</li>
 *     <li>status: status</li>
 *     <li>address: address</li>
 *     <li>provinceId: provinceId</li>
 *     <li>provinceName: provinceName</li>
 *     <li>cityId: cityId</li>
 *     <li>cityName: cityName</li>
 *     <li>areaId: areaId</li>
 *     <li>areaName: areaName</li>
 *     <li>longitude: longitude</li>
 *     <li>latitude: latitude</li>
 * </ul>
 */
public class UpdateCommunityPartialAdminCommand {
    @NotNull
    private Long communityId;

    private String name;

    private Byte status;

    private String address;
    private Long provinceId;
    private String provinceName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private Double longitude;
    private Double latitude;

    public UpdateCommunityPartialAdminCommand() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
