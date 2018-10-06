package com.everhomes.general.order;

import com.everhomes.rest.general.order.OrderCallBackCommand;

public interface GeneralOrderBizService {
	void orderCallBack(OrderCallBackCommand cmd);
}
