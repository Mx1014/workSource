// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 小区Id</li>
 * <li>name: 小区名称</li>
 * <li>regionId: 区域id(城市或区县)</li>
 * <li>regionName: 区域名</li>
 * <li>status: 小区状态，参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * </ul>
 */
public class SuggestCommunityDTO {
    private java.lang.Long     id;
    private java.lang.String   name;
    private java.lang.Long     regionId;
    private java.lang.String   regionName;
    
    private java.lang.Byte     status;

    public SuggestCommunityDTO() {
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
    
    public java.lang.Long getRegionId() {
        return regionId;
    }

    public void setRegionId(java.lang.Long regionId) {
        this.regionId = regionId;
    }

    public java.lang.String getRegionName() {
        return regionName;
    }

    public void setRegionName(java.lang.String regionName) {
        this.regionName = regionName;
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
