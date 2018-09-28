package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class TransformToCustomerCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;
	@ItemType(IntentionCustomerDTO.class)
	private List<IntentionCustomerDTO> intentionCustomers;
	
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

	public List<IntentionCustomerDTO> getIntentionCustomers() {
		return intentionCustomers;
	}

	public void setIntentionCustomers(List<IntentionCustomerDTO> intentionCustomers) {
		this.intentionCustomers = intentionCustomers;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
