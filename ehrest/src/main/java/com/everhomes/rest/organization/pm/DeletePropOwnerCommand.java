// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>contactToken: 业主手机号</li>
 * </ul>
 */
public class DeletePropOwnerCommand {
	@NotNull
    private Long communityId;
    @NotNull
    private String contactToken;
   
    public DeletePropOwnerCommand() {
    }

    public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
