// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryPeriod: 期数:类似201808</li>
 * <li>details: 导入工资条详情</li>
 * </ul>
 */
public class ImportPayslipResponse {

	private String salaryPeriod;

	@ItemType(PayslipDetailDTO.class)
	private List<PayslipDetailDTO> details;

	public ImportPayslipResponse() {

	}

	public ImportPayslipResponse(String salaryPeriod, List<PayslipDetailDTO> details) {
		super();
		this.salaryPeriod = salaryPeriod;
		this.details = details;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public List<PayslipDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PayslipDetailDTO> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
