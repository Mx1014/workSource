package com.everhomes.rest.pmsy;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>billDateStr：账单日期</li>
 * <li>MonthlyReceivableAmount: 账单应收金额</li>
 * <li>MonthlyDebtAmount: 账单待缴金额</li>
 * <li>requests: 账单详情列表  com.everhomes.rest.property.PmBillsDTO</li>
 * </ul>
 */
public class PmsyBillsDTO implements Comparable<PmsyBillsDTO>{
	private Long billDateStr;
	private BigDecimal monthlyReceivableAmount;
	private BigDecimal monthlyDebtAmount;
	@ItemType(PmsyBillItemDTO.class)
	private List<PmsyBillItemDTO> requests;
	
	public Long getBillDateStr() {
		return billDateStr;
	}
	public void setBillDateStr(Long billDateStr) {
		this.billDateStr = billDateStr;
	}
	
	
	public BigDecimal getMonthlyReceivableAmount() {
		return monthlyReceivableAmount;
	}
	public void setMonthlyReceivableAmount(BigDecimal monthlyReceivableAmount) {
		this.monthlyReceivableAmount = monthlyReceivableAmount;
	}
	public BigDecimal getMonthlyDebtAmount() {
		return monthlyDebtAmount;
	}
	public void setMonthlyDebtAmount(BigDecimal monthlyDebtAmount) {
		this.monthlyDebtAmount = monthlyDebtAmount;
	}
	public List<PmsyBillItemDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<PmsyBillItemDTO> requests) {
		this.requests = requests;
	}
	@Override
	public int compareTo(PmsyBillsDTO o) {
		if(this.billDateStr.longValue() < o.billDateStr.longValue())
			return -1;
		if(this.billDateStr.longValue() > o.billDateStr.longValue())
			return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
        if(this == obj)
        	return true;
        if(obj instanceof PmsyBillsDTO)
        	return this.billDateStr.longValue() == ((PmsyBillsDTO)obj).billDateStr.longValue();
        return false;
    }
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
