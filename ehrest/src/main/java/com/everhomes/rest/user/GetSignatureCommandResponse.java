package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id:用户id</li>
 * 	<li>name:用户名</li>
 * 	<li>signature:签名</li>
 * 	<li>appKey:公钥</li>
 * 	<li>timeStamp:时间戳</li>
 * 	<li>randomNum:随机数</li>
 * </ul>
 * 
 */
public class GetSignatureCommandResponse {
	private Long id;
	private String name;
	private String signature;
	private String appKey;
	private Long timeStamp;
	private Integer randomNum;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
