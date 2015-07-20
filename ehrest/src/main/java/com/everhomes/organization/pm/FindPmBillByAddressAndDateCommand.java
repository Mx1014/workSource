package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>billDate : 账单日期</li>
 *	<li>address : 地址</li>
 *</ul>
 *
 */
public class FindPmBillByAddressAndDateCommand {
	
	@NotNull
	private String billDate;
	@NotNull
	private String address;
	
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	 

}
