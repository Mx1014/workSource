package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDetailDTO;

/**
 * <ul>
 *     <li>userId: 用户id</li>
 *     <li>identityNumber: 身份证号</li>
 *     <li>organizations: 公司信息，organizationMemberDetailId必传，会对是否高管、职位进行更新 {@link com.everhomes.rest.organization.OrganizationDetailDTO}</li>
 * </ul>
 */
public class UpdateCommunityUserCommand {

	private Long userId;

	private String identityNumber;

	@ItemType(OrganizationDetailDTO.class)
	private List<OrganizationDetailDTO> organizations;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public List<OrganizationDetailDTO> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<OrganizationDetailDTO> organizations) {
		this.organizations = organizations;
	}
}
