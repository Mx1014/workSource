package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerUid: 用户ID</li>
 * <li>communityId: 用户所在小区或园区ID</li>
 * </ul>
 */
public class ListPostedActivityByOwnerIdCommand {
	 
	private Long ownerUid;
	    
	private Long communityId;
	
	public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
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
