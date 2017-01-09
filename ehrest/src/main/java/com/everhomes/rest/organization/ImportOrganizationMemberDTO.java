package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

public class ImportOrganizationMemberDTO {


	public ImportOrganizationMemberDTO(){
		super();
	}
	public ImportOrganizationMemberDTO(String employeeNo, String contactName, String contactToken, String gender, String departments, String jobPositions, String jobLevels) {
		this.employeeNo = employeeNo;
		this.contactName = contactName;
		this.contactToken = contactToken;
		this.gender = gender;
		this.departments = departments;
		this.jobPositions = jobPositions;
		this.jobLevels = jobLevels;
	}

	private String employeeNo;
	private String contactName;
	private String contactToken;
	private String gender;
	private String departments;
    private String jobPositions;
    private String jobLevels;
    private String description;
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDepartments() {
		return departments;
	}
	public void setDepartments(String departments) {
		this.departments = departments;
	}
	public String getJobPositions() {
		return jobPositions;
	}
	public void setJobPositions(String jobPositions) {
		this.jobPositions = jobPositions;
	}
	public String getJobLevels() {
		return jobLevels;
	}
	public void setJobLevels(String jobLevels) {
		this.jobLevels = jobLevels;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
