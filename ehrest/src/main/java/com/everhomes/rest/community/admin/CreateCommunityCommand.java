// @formatter:off

package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>name: 小区名称</li>
 *     <li>aliasName: 小区别名</li>
 *     <li>address: 小区地址</li>
 *     <li>description: 描述</li>
 *     <li>communityType: 园区类型，0住宅类型小区，1商用类型园区，参考{@link com.everhomes.rest.community.CommunityType}</li>
 *     <li>provinceId: 省id</li>
 *     <li>provinceName: 省名称</li>
 *     <li>cityId: 城市Id</li>
 *     <li>cityName: 城市名称</li>
 *     <li>areaId: 区域Id</li>
 *     <li>areaName: 区域名称</li>
 *     <li>longitude: 经度</li>
 *     <li>latitude: 纬度</li>
 *     <li>communityNumber: 编号</li>
 *     <li>pmOrgId: 管理公司id</li>
 *     <li>status: 参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * </ul>
 */
public class CreateCommunityCommand {
    private Integer namespaceId;
    private String name;
    private String aliasName;
    private String address;
    private String description;
    private Byte communityType;
    private Long provinceId;
    private String provinceName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private Double longitude;
    private Double latitude;
    private String communityNumber;
    private Long pmOrgId;
    private Byte status;
    private Long organizationId;


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public String getCommunityNumber() {
        return communityNumber;
    }

    public void setCommunityNumber(String communityNumber) {
        this.communityNumber = communityNumber;
    }

    public Long getPmOrgId() {
        return pmOrgId;
    }

    public void setPmOrgId(Long pmOrgId) {
        this.pmOrgId = pmOrgId;
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