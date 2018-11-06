package com.everhomes.rest.user;

import com.everhomes.rest.community.CommunityInfoDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 显示名称</li>
 *     <li>address: 地址信息</li>
 *     <li>provinceId: 省份Id</li>
 *     <li>provinceName: 省份名称</li>
 *     <li>cityId: 城市Id</li>
 *     <li>cityName: 城市名称</li>
 *     <li>areaId: 区域Id</li>
 *     <li>areaName: 区域名称</li>
 *     <li>longitude: 精度</li>
 *     <li>latitude: 纬度</li>
 *     <li>wholeAddressName: 办公地点名称全称</li>
 *     <li>communityName: 办公地点所属项目</li>
 * </ul>
 */
public class AddressSiteDTO {
    private Long id;
    private String name;
    private String address;
    private Long provinceId;
    private String provinceName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private Double longitude;
    private Double latitude;
    private String wholeAddressName;
    private String communityName;
    private Long communityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getWholeAddressName() {
        return wholeAddressName;
    }

    public void setWholeAddressName(String wholeAddressName) {
        this.wholeAddressName = wholeAddressName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
