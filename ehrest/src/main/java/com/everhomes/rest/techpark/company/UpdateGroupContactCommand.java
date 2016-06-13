package com.everhomes.rest.techpark.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class UpdateGroupContactCommand {
	@NotNull
	private Long id;
	private String contactName;
	private String contactToken;
	private String department;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
