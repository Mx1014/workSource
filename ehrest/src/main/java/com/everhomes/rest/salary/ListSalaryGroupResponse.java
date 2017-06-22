// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>salaryGroupId: 批次id</li>
 * <li>salaryGroupName: 批次名称</li>
 * <li>correlateNum: 批次关联人数</li>
 * </ul>
 */
public class ListSalaryGroupResponse {

	private Long salaryGroupId;

	private String salaryGroupName;

	private String correlateNum;

	public ListSalaryGroupResponse() {

	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public String getSalaryGroupName() {
		return salaryGroupName;
	}

	public void setSalaryGroupName(String salaryGroupName) {
		this.salaryGroupName = salaryGroupName;
	}

	public String getCorrelateNum() {
		return correlateNum;
	}

	public void setCorrelateNum(String correlateNum) {
		this.correlateNum = correlateNum;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
