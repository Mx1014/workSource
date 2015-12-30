package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>billDate : 账单日期</li>
 * <li>addressId : 地址Id</li>
 *</ul>
 *
 */
public class FindBillByAddressIdAndTimeCommand {
	
	@NotNull
	private String billDate;
	@NotNull
	private Long addressId;
	
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	 

}
