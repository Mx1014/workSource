package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/6/4 12 :36
 */

public class ImportOrganizationOwnerDTO {
    private String contactName = "";
    private String contactType = "";
    private String contactToken = "";
    private String gender = "";
    private String building = "";
    private String address = "";
    private String livingStatus="";
    private String livingTime="";
    private String birthday = "";
    private String maritalStatus = "";
    private String job = "";
    private String company = "";
    private String idCardNumber = "";
    private String registeredResidence = "";
    private String contactExtraTels = "";
    
    public String getContactExtraTels() {
		return contactExtraTels;
	}

	public void setContactExtraTels(String contactExtraTels) {
		this.contactExtraTels = contactExtraTels;
	}

	public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(String livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getLivingTime() {
        return livingTime;
    }

    public void setLivingTime(String livingTime) {
        this.livingTime = livingTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getRegisteredResidence() {
        return registeredResidence;
    }

    public void setRegisteredResidence(String registeredResidence) {
        this.registeredResidence = registeredResidence;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
