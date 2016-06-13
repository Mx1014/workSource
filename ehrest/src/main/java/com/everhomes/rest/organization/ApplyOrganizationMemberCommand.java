// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>organizationType : 组织类型 {@link com.everhomes.rest.organization.OrganizationType}</li>
 * <li>contactDescription：描述</li>
 * </ul>
 */
public class ApplyOrganizationMemberCommand {
    @NotNull
    private Long   communityId;
    
    @NotNull
	private String organizationType;
   
	private String contactDescription;
	
	public ApplyOrganizationMemberCommand() {
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
