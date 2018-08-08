package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul> 
 * <li>year: 工资条年份</li>
 * <li>monthPayslipDetails: 工资条列表{@link com.everhomes.rest.salary.MonthPayslipDetailDTO}</li>
 * </ul>
 */
public class PayslipYearDTO implements Comparable {
	private String year;

    private List<MonthPayslipDetailDTO> monthPayslipDetails;

	public PayslipYearDTO() {

	}

	public PayslipYearDTO(String year, List<MonthPayslipDetailDTO> monthPayslipDetails) {
		super();
		this.year = year;
		this.monthPayslipDetails = monthPayslipDetails;
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

	public List<MonthPayslipDetailDTO> getMonthPayslipDetails() {
		return monthPayslipDetails;
	}

	public void setMonthPayslipDetails(List<MonthPayslipDetailDTO> monthPayslipDetails) {
		this.monthPayslipDetails = monthPayslipDetails;
	}

	@Override
	public int compareTo(Object o) {
		try {
			PayslipYearDTO dto = (PayslipYearDTO) o;
				return Integer.valueOf(this.getYear()) < Integer.valueOf(this.getYear()) ? 1 : -1;
		} catch (Exception e) {
			//pass
		}
		return 0;
	}
}
