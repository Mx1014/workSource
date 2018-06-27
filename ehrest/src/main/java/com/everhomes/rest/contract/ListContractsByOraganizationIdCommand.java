// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:  
 * <li>organizationId: 公司id</li>
 * <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 */
public class ListContractsByOraganizationIdCommand {

	private Long organizationId;

	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
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
