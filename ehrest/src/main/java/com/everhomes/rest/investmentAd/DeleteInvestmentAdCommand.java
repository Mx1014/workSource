package com.everhomes.rest.investmentAd;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id: 招商广告id</li>
 * </ul>
 */
public class DeleteInvestmentAdCommand {
	
	@NotNull
	private Long id;
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
