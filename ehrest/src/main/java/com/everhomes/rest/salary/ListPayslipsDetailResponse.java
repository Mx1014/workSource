// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>detail: 工资条详情{@link com.everhomes.rest.salary.PayslipDetailDTO}</li>
 * </ul>
 */
public class ListPayslipsDetailResponse {

	private PayslipDetailDTO detail;

	public ListPayslipsDetailResponse() {

	}

	public ListPayslipsDetailResponse(PayslipDetailDTO detail) {
		super();
		this.detail = detail;
	}

	public PayslipDetailDTO getDetail() {
		return detail;
	}

	public void setDetail(PayslipDetailDTO detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
