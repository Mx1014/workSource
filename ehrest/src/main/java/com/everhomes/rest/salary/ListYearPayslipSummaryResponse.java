// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>monthPayslips:一年内的工资条月份列表{@link com.everhomes.rest.salary.MonthPayslipDTO}</li>
 * </ul>
 */
public class ListYearPayslipSummaryResponse {
	
	private List<MonthPayslipDTO> monthPayslips; 

	public ListYearPayslipSummaryResponse(){}
	public ListYearPayslipSummaryResponse(List<MonthPayslipDTO> monthPayslips){
		this.monthPayslips = monthPayslips;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public List<MonthPayslipDTO> getMonthPayslips() {
		return monthPayslips;
	}

	public void setMonthPayslips(List<MonthPayslipDTO> monthPayslips) {
		this.monthPayslips = monthPayslips;
	}
 

}
