package com.everhomes.rest.pmsy;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>billId: 账单id</li>
 * <li>billDateStr: 所属账期（费用所属账期）</li>
 * <li>receivableAmount: 应收金额</li>
 * <li>debtAmount: 欠收金额</li>
 * <li>customerId: 客户ID</li>
 * <li>itemName: 收费项目名称（此笔费用对应的名称，如“电费”，“水费”，“物业管理费”等）</li>
 * </ul>
 */
public class PmsyBillItemDTO {
	private String billId;
	private Long billDateStr;
	private BigDecimal receivableAmount;
	private BigDecimal debtAmount;
	private String customerId;
	private String itemName;
	

	public Long getBillDateStr() {
		return billDateStr;
	}
	public void setBillDateStr(Long billDateStr) {
		this.billDateStr = billDateStr;
	}
	
	public BigDecimal getReceivableAmount() {
		return receivableAmount;
	}
	public void setReceivableAmount(BigDecimal receivableAmount) {
		this.receivableAmount = receivableAmount;
	}
	public BigDecimal getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	
	
}
