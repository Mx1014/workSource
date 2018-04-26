package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul> 
 * <li>year: 工资条年份</li>
 * <li>payslipPeriods: 工资条列表{@link com.everhomes.rest.salary.PayslipPeriodDTO}</li>
 * </ul>
 */
public class PayslipYearDTO {
	private String year;
	@ItemType(PayslipPeriodDTO.class)
	private List<PayslipPeriodDTO> payslipPeriods;

	public PayslipYearDTO() {

	}

	public PayslipYearDTO(List<PayslipPeriodDTO> payslipPeriods) {
		super();
		this.payslipPeriods = payslipPeriods;
	}

	public List<PayslipPeriodDTO> getPayslipPeriods() {
		return payslipPeriods;
	}

	public void setPayslipPeriods(List<PayslipPeriodDTO> payslipPeriods) {
		this.payslipPeriods = payslipPeriods;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
