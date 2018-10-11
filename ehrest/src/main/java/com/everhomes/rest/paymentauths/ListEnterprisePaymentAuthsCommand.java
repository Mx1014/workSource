package com.everhomes.rest.paymentauths;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	   <li>namespaceId </li>
 *     <li>orgnazitionId: 公司ID</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthsCommand {
	private Long organizationId;
	private Integer namespaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
