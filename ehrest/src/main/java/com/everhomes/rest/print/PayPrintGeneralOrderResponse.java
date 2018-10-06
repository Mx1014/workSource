package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>generalOrderPageUrl : 统一订单支付页面</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月6日
 */
public class PayPrintGeneralOrderResponse {
	private String generalOrderPageUrl;
	
	
	public String getGeneralOrderPageUrl() {
		return generalOrderPageUrl;
	}


	public void setGeneralOrderPageUrl(String generalOrderPageUrl) {
		this.generalOrderPageUrl = generalOrderPageUrl;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
