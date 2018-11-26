package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 统一订单的订单id</li>
 * <li>merchantId : 商户id</li>
 * <li>payUrl:统一订单跳转链接</li>
 * </ul>
 */
public class CreateCubicleOrderBackgroundResponse {
	private Long orderId;
	private Long merchantId;
	private String payUrl;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	public Long getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	
}
