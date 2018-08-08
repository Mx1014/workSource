// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>payslipYears: 工资条年份列表{@link com.everhomes.rest.salary.PayslipYearDTO}</li>
 * </ul>
 */
public class ListUserPayslipsResponse {

	@ItemType(PayslipYearDTO.class)
	private List<PayslipYearDTO> payslipYears;

	public ListUserPayslipsResponse() {

	}


	public ListUserPayslipsResponse(List<PayslipYearDTO> payslipYears) {
		super();
		this.payslipYears = payslipYears;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public List<PayslipYearDTO> getPayslipYears() {
		return payslipYears;
	}


	public void setPayslipYears(List<PayslipYearDTO> payslipYears) {
		this.payslipYears = payslipYears;
	}

}
