package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>billId : 账单id</li>
 * 	<li>amount : 金额</li>
 *	<li>description : 描述</li>
 *</ul>
 *
 */
public class CreatePmBillOrderCommand {
	
	@NotNull
	private Long billId;
	private BigDecimal amount;
	private String description;
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	

}
