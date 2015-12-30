package com.everhomes.rest.statistics.admin;

public class ListStatisticsByCityDTO {
	
	private String cityName;
	
	private Integer registerConut;
	
	private Integer activeCount;

	private double regRatio;
	
	private Integer addressCount;
	
	private double addrRatio;
	
	private double cityActiveRatio;
	
	private double cityRegRatio;
	
	private double cityAddrRatio;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getRegisterConut() {
		return registerConut;
	}

	public void setRegisterConut(Integer registerConut) {
		this.registerConut = registerConut;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public double getRegRatio() {
		return regRatio;
	}

	public void setRegRatio(double regRatio) {
		this.regRatio = regRatio;
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

	public double getCityActiveRatio() {
		return cityActiveRatio;
	}

	public void setCityActiveRatio(double cityActiveRatio) {
		this.cityActiveRatio = cityActiveRatio;
	}

	public double getCityRegRatio() {
		return cityRegRatio;
	}

	public void setCityRegRatio(double cityRegRatio) {
		this.cityRegRatio = cityRegRatio;
	}

	public double getCityAddrRatio() {
		return cityAddrRatio;
	}

	public void setCityAddrRatio(double cityAddrRatio) {
		this.cityAddrRatio = cityAddrRatio;
	}
	

}
