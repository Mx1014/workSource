package com.everhomes.rest.pmsy;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>id : 物业缴费用户的ID(存在左邻这边的用户ID)</li>
 * <li>userName : 用户名称</li>
 * <li>userContact : 用户手机号</li>
 *</ul>
 *
 */
public class PmsyPayerDTO {
	
	private java.lang.Long     id;
	private java.lang.String   userName;
	private java.lang.String   userContact;
	
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
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
