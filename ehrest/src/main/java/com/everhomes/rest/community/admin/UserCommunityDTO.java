package com.everhomes.rest.community.admin;

/**
 * <ul>
 *  <li>communityId: 小区id</li>
 *  <li>communityName: 小区名称</li>
 *  <li>organizationId: 机构id</li>
 * </ul>
 */
public class UserCommunityDTO {
	
	private Long communityId;
	
	private String communityName;
	
	private Long organizationId;

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
