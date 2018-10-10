package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 统一订单的订单id</li>
 * <li>merchantId : 商户id</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月6日
 */
public class PayPrintGeneralOrderResponse {
	private Long orderId;
	private Long merchantId;
	
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
}
