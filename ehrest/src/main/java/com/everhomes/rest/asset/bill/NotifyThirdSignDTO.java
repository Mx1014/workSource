//@formatter:off
package com.everhomes.rest.asset.bill;

/**
 *<ul>
 * <li>billId: 账单ID，左邻对这些账单置一个特殊error标记，以此标记判断这些账单数据下一次同步不再传输</li>
 * <li>errorDescription：同步异常描述</li>
 */
public class NotifyThirdSignDTO {
	
	private Long billId;
	private String errorDescription;
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
}
