package com.everhomes.payment.util;

public class VerifyCodeEntity {
	private Long createTime;
	private String verifyCode;
	
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	public boolean isExpired() {
		return (createTime + 10 * 60 * 1000) < System.currentTimeMillis();
	}
	
}
