package com.everhomes.rest.investmentAd;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementOrders: 排序数组</li>
 * </ul>
 */
public class ChangeInvestmentAdOrderCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;
	@ItemType(InvestmentAdOrderDTO.class)
	private List<InvestmentAdOrderDTO> investmentAdOrders;

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

	public List<InvestmentAdOrderDTO> getInvestmentAdOrders() {
		return investmentAdOrders;
	}
	
	public void setInvestmentAdOrders(List<InvestmentAdOrderDTO> investmentAdOrders) {
		this.investmentAdOrders = investmentAdOrders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
