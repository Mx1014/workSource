package com.everhomes.general.order;

import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;

public interface GeneralOrderHandler {
	String GENERAL_ORDER_HANDLER = "GeneralOrderHandler-";

	CreateMerchantOrderResponse createOrder(Object cmd);
}
