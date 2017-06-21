// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>userId: 用户id</li>
 * <li>salaryGroupId: 批次id</li>
 * </ul>
 */
public class UpdateSalaryEmployeesCommand {

	private Long userId;

	private Long salaryGroupId;


	public UpdateSalaryEmployeesCommand() {

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
