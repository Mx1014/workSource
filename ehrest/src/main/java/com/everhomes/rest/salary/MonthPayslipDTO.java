package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul> 
 * <li>salaryPeriod: 期数</li>
 * <li>sendCount: 发放数</li>
 * </ul>
 */
public class MonthPayslipDTO {
	private String salaryPeriod;

	private Integer sendCount;

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
