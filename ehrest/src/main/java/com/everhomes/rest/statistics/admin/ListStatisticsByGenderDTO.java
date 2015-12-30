package com.everhomes.rest.statistics.admin;

public class ListStatisticsByGenderDTO {
	
	private String gender;
	
	private Integer registerConut;
	
	private Integer addressCount;
	
	private double addrRatio;
	
	private double genderRegRatio;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getRegisterConut() {
		return registerConut;
	}

	public void setRegisterConut(Integer registerConut) {
		this.registerConut = registerConut;
	}

	public Integer getAddressCount() {
		return addressCount;
	}

	public void setAddressCount(Integer addressCount) {
		this.addressCount = addressCount;
	}

	public double getAddrRatio() {
		return addrRatio;
	}

	public void setAddrRatio(double addrRatio) {
		this.addrRatio = addrRatio;
	}

	public double getGenderRegRatio() {
		return genderRegRatio;
	}

	public void setGenderRegRatio(double genderRegRatio) {
		this.genderRegRatio = genderRegRatio;
	}
	
	

}
