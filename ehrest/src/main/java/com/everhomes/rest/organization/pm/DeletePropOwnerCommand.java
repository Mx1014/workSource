// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>ownerId: 业主表主键id</li>
 * </ul>
 */
public class DeletePropOwnerCommand {
    private Long communityId;
    
    private Integer ownerId;
   
    public DeletePropOwnerCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
	public Integer getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
