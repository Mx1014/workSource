package com.everhomes.general.order;

import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;

public interface GeneralOrderBizHandler {
	String GENERAL_ORDER_HANDLER = "GeneralOrderHandler-";
	
	CreateMerchantOrderResponse createOrder(Object cmd);

	void dealCallBack(OrderCallBackCommand cmd);
	
}
