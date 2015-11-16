package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>siteUserToken : 第三方用户标识</li>
 * 	<li>siteUri : 链接</li>
 * 	<li>signature : 签名</li>
 * 	<li>randomNum : 随机数</li>
 * 	<li>timeStamp : 时间戳</li>
 * 	<li>appKey : 公钥</li>
 * <ul>
 * 
 */
public class SynThridUserCommand {
	@NotNull
	private String siteUserToken;
	@NotNull
	private String siteUri;
	
	@NotNull
	private String signature;
	@NotNull
	private Integer randomNum;
	@NotNull
	private Long timeStamp;
	@NotNull
	private String appKey;
	
	
	public String getSiteUserToken() {
		return siteUserToken;
	}
	public void setSiteUserToken(String siteUserToken) {
		this.siteUserToken = siteUserToken;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSiteUri() {
		return siteUri;
	}
	public void setSiteUri(String siteUri) {
		this.siteUri = siteUri;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
