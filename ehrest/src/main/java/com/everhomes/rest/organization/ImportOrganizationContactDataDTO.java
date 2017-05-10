package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


public class ImportOrganizationContactDataDTO {
	
	private String contactName = "";

	private String contactToken = "";

	private String gender = "";

	private String orgnaizationPath = "";

	private String jobPosition = "";

	private String jobLevel = "";

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

	public String getOrgnaizationPath() {
		return orgnaizationPath;
	}

	public void setOrgnaizationPath(String orgnaizationPath) {
		this.orgnaizationPath = orgnaizationPath;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
