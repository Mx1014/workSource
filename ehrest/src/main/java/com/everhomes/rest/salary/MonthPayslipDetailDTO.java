package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>salaryPeriod: 期数</li>
 * <li>updateTime: 发放时间</li>
 * <li>payslipDetailId: 详情id</li> 
 * </ul>
 */
public class MonthPayslipDetailDTO {
    private String salaryPeriod;
	private Long updateTime;
	private Long payslipDetailId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
 
	public Long getPayslipDetailId() {
		return payslipDetailId;
	}

	public void setPayslipDetailId(Long payslipDetailId) {
		this.payslipDetailId = payslipDetailId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
}
