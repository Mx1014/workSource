package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>totalDueOweAmount : 应付总额</li>
 * <li>totalPaidAmount : 实付总额</li>
 * <li>nowWaitPayAmount : 需支付金额</li>
 *</ul>
 *
 */
public class GetFamilyStatisticCommandResponse {
	
	private BigDecimal totalDueOweAmount;
	private BigDecimal totalPaidAmount;
	private BigDecimal nowWaitPayAmount;
	public BigDecimal getTotalDueOweAmount() {
		return totalDueOweAmount;
	}
	public void setTotalDueOweAmount(BigDecimal totalDueOweAmount) {
		this.totalDueOweAmount = totalDueOweAmount;
	}
	public BigDecimal getTotalPaidAmount() {
		return totalPaidAmount;
	}
	public void setTotalPaidAmount(BigDecimal totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}
	public BigDecimal getNowWaitPayAmount() {
		return nowWaitPayAmount;
	}
	public void setNowWaitPayAmount(BigDecimal nowWaitPayAmount) {
		this.nowWaitPayAmount = nowWaitPayAmount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
