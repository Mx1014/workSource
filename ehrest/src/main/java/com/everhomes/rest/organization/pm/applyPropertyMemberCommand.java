// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>contactDescription：描述</li>
 * </ul>
 */
public class applyPropertyMemberCommand {
    @NotNull
    private Long   communityId;
   
	private String contactDescription;
	
	public applyPropertyMemberCommand() {
    }
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	public String getContactDescription() {
		return contactDescription;
	}
	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
