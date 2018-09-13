// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authList:授权申请信息列表 {@link com.everhomes.rest.aclink.OpenAuthDTO}</li>
 * <li>macAddresses：门禁mac地址列表</li>
 * <li>validFromMs: 有效期开始时间</li>
 * <li>validEndMs: 有效期终止时间</li>
 * </ul>
 *
 */
public class BatchCreateVisitorsCommand {
	private List<OpenAuthDTO> authList;
	private List<String> macAddresses;
	private Long validFromMs;
	private Long validEndMs;
	private String appKey;
	private String signature;
	private Long timestamp;
	private Integer nonce;
	private String crypto;
	public Long getValidFromMs() {
		return validFromMs;
	}

	public void setValidFromMs(Long validFromMs) {
		this.validFromMs = validFromMs;
	}

	public Long getValidEndMs() {
		return validEndMs;
	}

	public void setValidEndMs(Long validEndMs) {
		this.validEndMs = validEndMs;
	}
 
	public List<OpenAuthDTO> getAuthList() {
		return authList;
	}

	public void setAuthList(List<OpenAuthDTO> authList) {
		this.authList = authList;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<String> getMacAddresses() {
		return macAddresses;
	}

	public void setMacAddresses(List<String> macAddresses) {
		this.macAddresses = macAddresses;
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
	
}
