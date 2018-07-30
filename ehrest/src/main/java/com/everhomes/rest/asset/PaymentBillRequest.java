package com.everhomes.rest.asset;

import java.math.BigDecimal;

/**
*@author created by yangcx
*@date 2018年5月23日---下午5:50:43
**/
/**
 *<ul>
* <li>billId:账单id</li>
* <li>billGroupName:账单组名称</li>
 * <li>dateStr:账期</li>
* <li>dateStrBegin:账期开始时间</li>
 * <li>dateStrEnd:账期结束时间</li>
* <li>amountOwed:欠收(元)</li>
 *</ul>
 */
public class PaymentBillRequest {
	private Long billId;
	private String billGroupName;
	private String dateStr;
	private String dateStrBegin;
	private String dateStrEnd;
	private BigDecimal amountOwed;
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getBillGroupName() {
		return billGroupName;
	}
	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}
	public BigDecimal getAmountOwed() {
		return amountOwed;
	}
	public void setAmountOwed(BigDecimal amountOwed) {
		this.amountOwed = amountOwed;
	}

}
