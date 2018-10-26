package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifierToken:手机号</li>
 * <li>routeName:路线名称</li>
 * <li>nextStation:下一站名称</li>
 * <li>msgSendType:消息类型：1，只发应用内消息；2，只推送；3，同时推送兼发应用内消息</li>
 * <li>msgType:消息类型：1，只发应用内消息；2，只推送；3，同时推送兼发应用内消息</li>
 * <li>appkey: (必填)appkey</li>
 * <li>appsecret: 应用密钥AppSecret</li>
 * <li>messageType: 1：登车提醒；2：下车提醒；</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */

public class ThirdPartPushMessageCommand {
	private String identifierToken;
	private String routeName;
	private String nextStation;
	private Integer msgSendType;
	private Integer msgType;
	private String appkey;
	private String appsecret;
	
	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
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

	public Integer getMsgSendType() {
		return msgSendType;
	}

	public void setMsgSendType(Integer msgSendType) {
		this.msgSendType = msgSendType;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
