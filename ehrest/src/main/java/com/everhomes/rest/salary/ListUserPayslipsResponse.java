// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>payslipPeriods: 工资条列表{@link com.everhomes.rest.salary.PayslipPeriodDTO}</li>
 * </ul>
 */
public class ListUserPayslipsResponse {

	@ItemType(PayslipPeriodDTO.class)
	private List<PayslipPeriodDTO> payslipPeriods;

	public ListUserPayslipsResponse() {

	}

	public ListUserPayslipsResponse(List<PayslipPeriodDTO> payslipPeriods) {
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

}
