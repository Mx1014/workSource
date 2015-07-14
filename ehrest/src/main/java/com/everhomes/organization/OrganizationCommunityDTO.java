// @formatter:off
package com.everhomes.organization;

import com.everhomes.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>organizationId：机构id</li>
 * <li>communityId：小区id</li>
 * <li>communityName：小区名称</li>
 * </ul>
 */
public class OrganizationCommunityDTO {
	
	private Long id;
	private Long organizationId;
	private Long communityId;
	private String communityName;
	
	public OrganizationCommunityDTO() {
		
	}
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



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



	public String getCommunityName() {
		return communityName;
	}



	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
