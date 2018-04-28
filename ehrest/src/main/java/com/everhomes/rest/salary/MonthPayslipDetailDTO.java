package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>salaryPeriod: 期数</li>
 * <li>createTime: 发放时间</li>
 * <li>payslipDetailId: 详情id</li> 
 * </ul>
 */
public class MonthPayslipDetailDTO {
    private String salaryPeriod;
	private Long createTime;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
}
