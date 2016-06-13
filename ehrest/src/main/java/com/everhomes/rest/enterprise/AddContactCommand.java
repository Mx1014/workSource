package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

public class AddContactCommand {

    private java.lang.Long     enterpriseId;
    private java.lang.String   name;  
    private String employeeNo;
    private Long groupId;
    private String sex;
    private String phone;
    private java.lang.Long     role;
    

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 
	
	
	
	public java.lang.Long getRole() {
		return role;
	}
	public void setRole(java.lang.Long role) {
		this.role = role;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	} 
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(java.lang.Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}



	public Long getGroupId() {
		return groupId;
	}



	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
