package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>contact: 手机号</li>
 *  <li>employeeNo: 员工工号</li>
 * </ul>
 */
public class GroupUserDTO {
	
	private Long userId;
	
	private String userName;
	
	private String contact;
	
	private String employeeNo;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
