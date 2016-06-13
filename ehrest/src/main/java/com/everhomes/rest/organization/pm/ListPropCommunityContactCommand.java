// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>organizationType: 组织类型</li>
 * </ul>
 */
public class ListPropCommunityContactCommand {
    private Long communityId;
    private String organizationType;
   
    public ListPropCommunityContactCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	public String getOrganizationType() {
        return organizationType;
    }


    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
