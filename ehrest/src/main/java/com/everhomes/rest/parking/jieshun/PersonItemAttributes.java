package com.everhomes.rest.parking.jieshun;

import com.everhomes.util.StringHelper;

public class PersonItemAttributes {
	private String personCode;
	private String personName;
	private String identityCode;
	private String telephone;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
