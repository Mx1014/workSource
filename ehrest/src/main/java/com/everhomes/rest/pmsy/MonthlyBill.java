package com.everhomes.rest.pmsy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>billDateStr：账单日期</li>
 * <li>MonthlyReceivableAmount: 账单应收金额</li>
 * <li>MonthlyDebtAmount: 账单待缴金额</li>
 * <li>requests: 账单详情列表  com.everhomes.rest.property.PmBillsDTO</li>
 * </ul>
 */
public class MonthlyBill {
	private Long billDateStr;
	private BigDecimal MonthlyReceivableAmount;
	private BigDecimal MonthlyDebtAmount;
	@ItemType(PmBillItemDTO.class)
	private List<PmBillItemDTO> requests;
	
	public Long getBillDateStr() {
		return billDateStr;
	}
	public void setBillDateStr(Long billDateStr) {
		this.billDateStr = billDateStr;
	}
	public BigDecimal getMonthlyReceivableAmount() {
		return MonthlyReceivableAmount;
	}
	public void setMonthlyReceivableAmount(BigDecimal monthlyReceivableAmount) {
		MonthlyReceivableAmount = monthlyReceivableAmount;
	}
	public BigDecimal getMonthlyDebtAmount() {
		return MonthlyDebtAmount;
	}
	public void setMonthlyDebtAmount(BigDecimal monthlyDebtAmount) {
		MonthlyDebtAmount = monthlyDebtAmount;
	}
	
	
}
