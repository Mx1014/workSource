package com.everhomes.rest.techpark.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class CreateGroupContactCommand {
	@NotNull
	private Long ownerId;
	@NotNull
	private String contactName;
	@NotNull
	private String contactToken;
	private String department;
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactToken() {
		return contactToken;
	}
	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
