package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>siteUserToken : 第三方用户标识</li>
 * 	<li>siteUri : 链接</li>
 * 	<li>signature : 签名</li>
 * 	<li>randomNum : 随机数</li>
 * 	<li>timestamp : 时间戳</li>
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
	private Integer randomNum;
	@NotNull
	private Long timestamp;
	
	
	public String getSiteUserToken() {
		return siteUserToken;
	}
	public void setSiteUserToken(String siteUserToken) {
		this.siteUserToken = siteUserToken;
	}
	public Integer getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
