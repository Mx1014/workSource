// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: organizationId</li>
 * </ul>
 */
public class DeleteSalaryGroupCommand {

	private Long salaryGroupId;

	private String ownerType;

	private Long ownerId;

	public DeleteSalaryGroupCommand() {

	}

	public DeleteSalaryGroupCommand(Long salaryGroupId) {
		super();
		this.salaryGroupId = salaryGroupId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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
