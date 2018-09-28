package com.everhomes.rest.paymentauths;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: 企业支付授权应用ID(如云打印，资源预约等)</li>
 *     <li>appName: 企业支付授权应用名字</li>
 *     <li>EnterpiresAuthDTO: 授权用户信息</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthsResponse {
	private Long appId;
	private String appName;
	private List<EnterpiresAuthDTO> enterpriseAuth;


	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<EnterpiresAuthDTO> getEnterpriseAuth() {
		return enterpriseAuth;
	}

	public void setEnterpriseAuth(List<EnterpiresAuthDTO> enterpriseAuth) {
		this.enterpriseAuth = enterpriseAuth;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
