// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 小区Id</li>
 * <li>name: 小区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id</li>
 * <li>areaName: 区域名称</li>
 * <li>status: 小区状态，参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * </ul>
 */
public class CommunitySummaryDTO {
    private java.lang.Long     id;
    private java.lang.String   name;
    private java.lang.Long     cityId;
    private java.lang.String   cityName;
    private java.lang.Long     areaId;
    private java.lang.String   areaName;
    
    private java.lang.Byte     status;

    public CommunitySummaryDTO() {
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.Long getCityId() {
        return cityId;
    }

    public void setCityId(java.lang.Long cityId) {
        this.cityId = cityId;
    }

    public java.lang.String getCityName() {
        return cityName;
    }

    public void setCityName(java.lang.String cityName) {
        this.cityName = cityName;
    }

    public java.lang.Long getAreaId() {
        return areaId;
    }

    public void setAreaId(java.lang.Long areaId) {
        this.areaId = areaId;
    }

    public java.lang.String getAreaName() {
        return areaName;
    }

    public void setAreaName(java.lang.String areaName) {
        this.areaName = areaName;
    }
    
    public java.lang.Byte getStatus() {
        return status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
