package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.videoconf.ListVideoConfAccountOrderCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderResponse;
import com.everhomes.videoconf.ConfOrders;

public interface ConfOrderSearcher {

	void deleteById(Long id);
	void bulkUpdate(List<ConfOrders> orders);
    void feedDoc(ConfOrders order);
    void syncFromDb();
    ListVideoConfAccountOrderResponse query(ListVideoConfAccountOrderCommand cmd);
}
