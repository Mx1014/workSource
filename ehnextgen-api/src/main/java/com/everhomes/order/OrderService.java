package com.everhomes.order;

import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.RefundCallbackCommand;

public interface OrderService {

	void payCallback(PayCallbackCommand cmd);
	void refundCallback(RefundCallbackCommand cmd);

}
