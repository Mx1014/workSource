package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>billDate : 账单日期</li>
 * <li>familyId : 家庭Id</li>
 *</ul>
 *
 */
public class FindFamilyBillAndPaysByFamilyIdAndTimeCommand {
	
	@NotNull
	private String billDate;
	@NotNull
	private Long familyId;
	
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
	public Long getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
