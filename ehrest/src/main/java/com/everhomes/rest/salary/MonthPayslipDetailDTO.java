package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>operateTime: 发放时间</li>
 * <li>payslipDetailId: 详情id</li> 
 * </ul>
 */
public class MonthPayslipDetailDTO {
	private Long operateTime;
	private Long payslipDetailId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}

	public Long getPayslipDetailId() {
		return payslipDetailId;
	}

	public void setPayslipDetailId(Long payslipDetailId) {
		this.payslipDetailId = payslipDetailId;
	}
}
