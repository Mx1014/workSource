package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>contactId: 用户在企业通讯录中的id</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>department: 部门名称</li>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>mobile: 手机号</li>
 * </ul>
 *
 */
public class EnterpriseUsersDTO {
	
	private String department;
	
	private Long userId;
	
	private String userName;
	
	private String mobile;
	
	private Long contactId;
	
	private Long enterpriseId;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
