package com.everhomes.rest.openapi;
import com.everhomes.util.StringHelper;


public class GetOrgCheckInDataCommand {
	private String appKey;
	private String signature;
	private String timestamp;
	private String nonce;
	private String crypto;
	private String beginDate;
	private String endDate;
	private String orgId;
	private String userId;
	private String userContactToken;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getCrypto() {
		return crypto;
	}
	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserContactToken() {
		return userContactToken;
	}
	public void setUserContactToken(String userContactToken) {
		this.userContactToken = userContactToken;
	}

}
