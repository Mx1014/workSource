package com.everhomes.rest.pmsy;

/**
 *<ul>
 * <li>payerId : 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 * <li>userName : 用户名称</li>
 * <li>userContact : 用户手机号</li>
 *</ul>
 *
 */
public class PmPayerDTO {
	private Long payerId;
	private String userName;
	private String userContact;
	
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	
	
}
