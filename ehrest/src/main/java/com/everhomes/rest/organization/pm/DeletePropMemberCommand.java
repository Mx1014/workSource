// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 物业id</li>
 * <li>memberId: 物业成员id</li>
 * </ul>
 */
public class DeletePropMemberCommand {
	@NotNull
    private Long communityId;
    @NotNull
    private Integer memberId;
   
    public DeletePropMemberCommand() {
    }

	public Integer getMemberId() {
		return memberId;
	}


	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
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
