// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * add parameter communityId by liujinwen at 2016-05-12
 */

/**
 * <ul>
 * <li>buildingName: 楼栋名</li>
 * <li>buildingAliasName: 楼栋别名</li>
 * <li>communityId: 小区id</li>
 * </ul>
 */
public class BuildingDTO {
    private java.lang.String   buildingName;
    private java.lang.String   buildingAliasName;
    private java.lang.String   businessBuildingnName;
    private Long communityId;
    
    public java.lang.String getBusinessBuildingnName() {
		return businessBuildingnName;
	}

	public void setBusinessBuildingnName(java.lang.String businessBuildingnName) {
		this.businessBuildingnName = businessBuildingnName;
	}

	public BuildingDTO() {
    }

    public java.lang.String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(java.lang.String buildingName) {
        this.buildingName = buildingName;
    }

    public java.lang.String getBuildingAliasName() {
        return buildingAliasName;
    }

    public void setBuildingAliasName(java.lang.String buildingAliasName) {
        this.buildingAliasName = buildingAliasName;
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
