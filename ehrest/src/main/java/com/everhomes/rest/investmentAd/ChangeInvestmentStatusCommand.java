package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

public class ChangeInvestmentStatusCommand {

	private Long id;
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;
	private Byte investmentStatus;
	
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
	public Byte getInvestmentStatus() {
		return investmentStatus;
	}
	public void setInvestmentStatus(Byte investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
