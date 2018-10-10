package com.everhomes.rest.paymentauths;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: 企业支付授权应用ID(如云打印，资源预约等)</li>
 *     <li>EnterpiresAuthDTO: 授权用户信息</li>
 * </ul>
 */
public class EnterprisePaymentAuthsDTO {
	private Long appId;
	private List<EnterpriesAuthDTO> enterpriseAuth;


	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public List<EnterpriesAuthDTO> getEnterpriseAuth() {
		return enterpriseAuth;
	}

	public void setEnterpriseAuth(List<EnterpriesAuthDTO> enterpriseAuth) {
		this.enterpriseAuth = enterpriseAuth;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
