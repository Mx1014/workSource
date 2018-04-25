package com.everhomes.rest.salary;

import java.util.List;

/**
 * 
 * <ul> 
 * <li>salaryPeriod: 期数YYYYMM</li>
 * <li>monthPayslipDetails: 每月工资条详情列表{@link com.everhomes.rest.salary.MonthPayslipDetailDTO}</li> 
 * </ul>
 */
public class PayslipPeriodDTO {
    private String salaryPeriod;
    private List<MonthPayslipDetailDTO> monthPayslipDetails;
	public String getSalaryPeriod() {
		return salaryPeriod;
	}
	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
	public List<MonthPayslipDetailDTO> getMonthPayslipDetails() {
		return monthPayslipDetails;
	}
	public void setMonthPayslipDetails(List<MonthPayslipDetailDTO> monthPayslipDetails) {
		this.monthPayslipDetails = monthPayslipDetails;
	}
    
}
