package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

public class GetCustomerActivitySignupInfoCommand {
	
	private Integer namespaceId;
	private Long orgId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
