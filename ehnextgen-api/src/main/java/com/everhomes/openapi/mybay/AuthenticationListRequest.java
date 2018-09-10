package com.everhomes.openapi.mybay;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 深圳湾携程人事信息批量更新接口参数类
 * @author huanglm 20180619
 *
 */
public class AuthenticationListRequest {
	//private String Language ;
	/**
	 * 公司登陆ID(obk账户名)，由携程方提供,不能为空
	 */
	 @JSONField(name = "Appkey")
	private String Appkey ;
	/**
	 * 身份验证Token口令,不能为空
	 */
	 @JSONField  (name = "Ticket")
	private String Ticket ;
	/**
	 * 公司ID，由携程方提供,不能为空
	 */
	 @JSONField  (name = "CorporationID")
	private String CorporationID ;
	/**
	 * 验证实体,不能为空
	 */
	 @JSONField  (name = "AuthenticationInfoList")
	private List<AuthenticationInfo> AuthenticationInfoList ;

	public String getAppkey() {
		return Appkey;
	}
	public void setAppkey(String appkey) {
		Appkey = appkey;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	public String getCorporationID() {
		return CorporationID;
	}
	public void setCorporationID(String corporationID) {
		CorporationID = corporationID;
	}
	public List<AuthenticationInfo> getAuthenticationInfoList() {
		return AuthenticationInfoList;
	}
	public void setAuthenticationInfoList(
			List<AuthenticationInfo> authenticationInfoList) {
		AuthenticationInfoList = authenticationInfoList;
	}
	

}
