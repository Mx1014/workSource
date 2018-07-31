package com.everhomes.rest.openapi;
import com.everhomes.util.StringHelper;


public class GetOrgCheckInDataCommand {
	private String appKey;
	private String signature;
	private Long timestamp;
	private Integer nonce;
	private String crypto;
	private Long beginDate;
	private Long endDate;
	private Long orgId;
	private Long userId;
	private String userContactToken; 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

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

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getNonce() {
		return nonce;
	}

	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

	public String getCrypto() {
		return crypto;
	}

	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserContactToken() {
		return userContactToken;
	}

	public void setUserContactToken(String userContactToken) {
		this.userContactToken = userContactToken;
	} 
    

}
