// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>familyId: 家庭id</li>
 * <li>userId: 家庭成员userId</li>
 * </ul>
 */
public class CommunityPropFamilyMemberCommand {
    private Long communityId;
    
    private Long familyId;
    
    private Long userId;
    
    
    public CommunityPropFamilyMemberCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}

    public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public Long getFamilyId() {
		return familyId;
	}


	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
