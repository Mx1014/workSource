package com.everhomes.rest.asset.bill;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 统一订单的订单id</li>
 * <li>merchantId : 商户id</li>
 * </ul>
 */

/**
 * @author created by ycx
 * @date 2018年12月10日 下午1:59:00
 */
public class PayAssetGeneralOrderResponse {
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
