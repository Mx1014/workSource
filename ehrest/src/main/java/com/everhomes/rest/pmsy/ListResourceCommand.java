package com.everhomes.rest.pmsy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userName: 用户姓名</li>
 * <li>userContact: 用户手机号</li>
 * <li>payerId: 物业缴费用户的ID(存在左邻这边的用户ID,如果是新用户名和手机号,则不传)</li>
 * </ul>
 */
public class ListResourceCommand {
	private String userName;
	private String userContact;
	private Long payerId;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
