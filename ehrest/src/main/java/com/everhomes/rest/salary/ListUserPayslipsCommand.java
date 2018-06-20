// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 可选</li>
 * </ul>
 */
public class ListUserPayslipsCommand {

	private Long organizationId;

	private Long ownerId;

	public ListUserPayslipsCommand() {

	}

	public ListUserPayslipsCommand(Long organizationId, Long ownerId) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
