// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:  
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class ListContractsByOraganizationIdCommand {

	private Long organizationId;
	
	public ListContractsByOraganizationIdCommand() {

	}
 
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
