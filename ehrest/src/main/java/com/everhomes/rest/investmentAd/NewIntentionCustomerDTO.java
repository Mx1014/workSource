package com.everhomes.rest.investmentAd;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>applyEntryId：申请记录id</li> 
* <li>customerName：承租方（即企业客户名称）</li> 
* <li>addressIds：意向房源id(数组,可能会有多个意向房源)</li> 
* <li>applyUserName：申请人（即客户联系人）</li>
* <li>applyContact：申请人电话（即客户联系人电话）</li>
* </ul>
*/
public class NewIntentionCustomerDTO {
	
	private Long applyEntryId;
	private String customerName;
	private List<Long> addressIds;
	private String applyUserName;
	private String applyContact;
	
	public Long getApplyEntryId() {
		return applyEntryId;
	}
	public void setApplyEntryId(Long applyEntryId) {
		this.applyEntryId = applyEntryId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<Long> getAddressIds() {
		return addressIds;
	}
	public void setAddressIds(List<Long> addressIds) {
		this.addressIds = addressIds;
	}
	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	public String getApplyContact() {
		return applyContact;
	}
	public void setApplyContact(String applyContact) {
		this.applyContact = applyContact;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
