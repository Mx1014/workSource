// @formatter:off
package com.everhomes.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>buildingName: 小区楼栋号</li>
 * <li>apartmentName: 小区门牌号</li>
 * </ul>
 */
public class PropCommunityBuildAddessCommand {
    private Long communityId;
    
    private String buildingName;
    
    private String apartmentName;
   
    public PropCommunityBuildAddessCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	

	public String getBuildingName() {
		return buildingName;
	}


	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	

	public String getApartmentName() {
		return apartmentName;
	}


	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
