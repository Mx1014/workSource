package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>billId : 账单id</li>
 *	<li>description : 描述</li>
 *</ul>
 *
 */
public class CreatePmBillOrderCommand {
	
	@NotNull
	private Long billId;
	private String description;
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
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
