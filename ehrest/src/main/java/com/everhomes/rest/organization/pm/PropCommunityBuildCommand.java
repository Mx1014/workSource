// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>buildingName: 小区楼栋号</li>
 * </ul>
 */
public class PropCommunityBuildCommand {
    private Long communityId;
    
    private String buildingName;
   
    public PropCommunityBuildCommand() {
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
