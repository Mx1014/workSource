package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>serviceUserId: 客服人员id</li>
 * <li>serviceUserName: 客服人员姓名</li>
 * <li>serviceUserPhone: 客服人员电话</li>
 * </ul>
 */
public class OrganizationServiceUser {
	private Long serviceUserId;
	private String serviceUserName;
	private String serviceUserPhone;
	
	public OrganizationServiceUser() {
		super();
	}
	public OrganizationServiceUser(Long serviceUserId, String serviceUserName, String serviceUserPhone) {
		super();
		this.serviceUserId = serviceUserId;
		this.serviceUserName = serviceUserName;
		this.serviceUserPhone = serviceUserPhone;
	}
	public Long getServiceUserId() {
		return serviceUserId;
	}
	public void setServiceUserId(Long serviceUserId) {
		this.serviceUserId = serviceUserId;
	}
	public String getServiceUserName() {
		return serviceUserName;
	}
	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}
	public String getServiceUserPhone() {
		return serviceUserPhone;
	}
	public void setServiceUserPhone(String serviceUserPhone) {
		this.serviceUserPhone = serviceUserPhone;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
