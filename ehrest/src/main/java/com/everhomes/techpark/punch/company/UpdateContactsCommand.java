package com.everhomes.techpark.punch.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class UpdateContactsCommand {
	@NotNull
	private Long id;
	private String name;
	private String telephone;
	private String department;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
