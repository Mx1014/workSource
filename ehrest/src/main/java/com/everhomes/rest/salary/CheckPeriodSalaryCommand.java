// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryPeriodGroupId: 某期薪酬批次id</li>
 * </ul>
 */
public class CheckPeriodSalaryCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryPeriodGroupId;

	public CheckPeriodSalaryCommand() {

	}

	public CheckPeriodSalaryCommand(String ownerType, Long ownerId, Long salaryPeriodGroupId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryPeriodGroupId = salaryPeriodGroupId;
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

	public Long getSalaryPeriodGroupId() {
		return salaryPeriodGroupId;
	}

	public void setSalaryPeriodGroupId(Long salaryPeriodGroupId) {
		this.salaryPeriodGroupId = salaryPeriodGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
