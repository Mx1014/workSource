package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>communityId: 小区id</li>
 * </ul>
 */
public class ImportOwnerDataCommand {
	
	private Long organizationId;
	
	private Long communityId;
	
	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
