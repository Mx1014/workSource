package com.everhomes.general.order;

import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;

public interface GeneralOrderBizHandler {
	String GENERAL_ORDER_HANDLER = "GeneralOrderHandler-";

	void dealCallBack(OrderCallBackCommand cmd);

	CreateMerchantOrderResponse createOrder(CreateOrderBaseInfo baseInfo);

}
