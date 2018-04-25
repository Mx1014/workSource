package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

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
