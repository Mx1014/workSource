package com.everhomes.order;

import com.everhomes.rest.order.PayCallbackCommand;

public interface OrderService {

	void payCallback(PayCallbackCommand cmd);

}
