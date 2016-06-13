// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>memberId: 物业成员表主键id</li>
 * </ul>
 */
public class CommunityPropMemberCommand {
    private Long communityId;
    
    private Integer memberId;
   
    public CommunityPropMemberCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public Integer getMemberId() {
		return memberId;
	}


	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
