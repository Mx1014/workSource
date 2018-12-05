package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifierTokenList:目标用户的手机号列表，英文字符';'分割</li>
 * <li>routeName:路线名称</li>
 * <li>nextStation:下一站名称</li>
 * <li>appKey: (必填)appKey</li>
 * <li>secretKey : 应用密钥secretKey </li>
 * <li>messageType: 1：登车提醒；2：下车提醒；</li>
 * <li>timestamp:时间戳，使用 1970-01-01 00:00:00 开始到现在的毫秒数；</li>
 * <li>nonce:随机数</li>
 * <li>signature:请求签名，鉴别是否被篡改</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */
public class ThirdPartPushMessageCommand {
	private String identifierTokenList;
	private String appKey;
	private String secretKey;
	private String routeName;
	private String nextStation;
	private Integer msgType;
	private Long timestamp;
	private Integer nonce;
	private String signature;
	
	public String getIdentifierTokenList() {
		return identifierTokenList;
	}

	public void setIdentifierTokenList(String identifierTokenList) {
		this.identifierTokenList = identifierTokenList;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getNextStation() {
		return nextStation;
	}

	public void setNextStation(String nextStation) {
		this.nextStation = nextStation;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
