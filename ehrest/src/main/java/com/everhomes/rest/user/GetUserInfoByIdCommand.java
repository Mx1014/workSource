package com.everhomes.rest.user;

public class GetUserInfoByIdCommand {
	private String zlAppKey;
	private String zlSignature;
	private Long id;
	private String name;
	private Integer randomNum;
	private Long timeStamp;
	public String getZlAppKey() {
		return zlAppKey;
	}
	public void setZlAppKey(String zlAppKey) {
		this.zlAppKey = zlAppKey;
	}
	public String getZlSignature() {
		return zlSignature;
	}
	public void setZlSignature(String zlSignature) {
		this.zlSignature = zlSignature;
	}
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
	public Integer getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	

}
