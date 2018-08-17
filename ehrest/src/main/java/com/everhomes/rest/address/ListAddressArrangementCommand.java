// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId:房源id</li>
 * <li>organizationId:机构id</li>
 * </ul>
 */
public class ListAddressArrangementCommand {
	
	private Long addressId;
	private Long organizationId;
	private Integer namespaceId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
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
