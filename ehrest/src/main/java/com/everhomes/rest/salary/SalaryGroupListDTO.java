package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>salaryGroupId: 薪酬组id</li>
 * <li>salaryGroupName: 薪酬组名称</li>
 * <li>relevantNum: 薪酬组相关人数</li>
 * </ul>
 */
public class SalaryGroupListDTO {
	private Long salaryGroupId;

	private String salaryGroupName;

	private Integer relevantNum;

	public SalaryGroupListDTO() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Integer getRelevantNum() {
		return relevantNum;
	}

	public void setRelevantNum(Integer relevantNum) {
		this.relevantNum = relevantNum;
	}
}
