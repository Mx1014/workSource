package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifierTokenList:目标用户的手机号列表，英文字符';'分割</li>
 * <li>routeName:路线名称</li>
 * <li>nextStation:下一站名称</li>
 * <li>appkey: (必填)appkey</li>
 * <li>appsecret: 应用密钥AppSecret</li>
 * <li>messageType: 1：登车提醒；2：下车提醒；</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */

public class ThirdPartPushMessageCommand {
	private String identifierTokenList;
	private String appkey;
	private String appsecret;
	private String routeName;
	private String nextStation;
	private Integer msgType;
	
	public String getIdentifierTokenList() {
		return identifierTokenList;
	}

	public void setIdentifierTokenList(String identifierTokenList) {
		this.identifierTokenList = identifierTokenList;
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
