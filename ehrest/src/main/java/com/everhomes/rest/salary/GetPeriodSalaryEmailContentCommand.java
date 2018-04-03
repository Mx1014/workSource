// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryOrgId:  批次id</li>
 * </ul>
 */
public class GetPeriodSalaryEmailContentCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryOrgId;

	public GetPeriodSalaryEmailContentCommand() {

	}

	public GetPeriodSalaryEmailContentCommand(String ownerType, Long ownerId, Long salaryOrgId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryOrgId = salaryOrgId;
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

	public Long getSalaryOrgId() {
		return salaryOrgId;
	}

	public void setSalaryOrgId(Long salaryOrgId) {
		this.salaryOrgId = salaryOrgId;
	}
}
