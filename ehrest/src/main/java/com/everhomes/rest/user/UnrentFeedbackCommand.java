package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>appKey : 应用Key</li>
 * 	<li>signature : 对所有参数进行SHA1加密后的签名</li>
 * 	<li>timestamp : 时间戳/li>
 * 	<li>nonce : 随机数</li>
 * 	<li>crypto : 参数value加密算法名，有值时表示对指定的参数进行加密；无值则表示参数value不加密；</li>
 * 	<li>type : 申请类型：1.用户认证，2.企业认证</li>
 * 	<li>phone : 公钥</li>
 * <ul>
 * 
 */
public class UnrentFeedbackCommand {
	@NotNull
	private String appKey;
	@NotNull
	private String signature;
	@NotNull
	private Long timestamp;
	@NotNull
	private Integer nonce;
	@NotNull
	private String crypto;
	@NotNull
	private Byte type;
	@NotNull
	private String phone;
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
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
