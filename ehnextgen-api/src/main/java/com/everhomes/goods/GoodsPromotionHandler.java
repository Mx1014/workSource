package com.everhomes.goods;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

public interface GoodsPromotionHandler {

	String GOODS_PROMOTION_HANDLER_PREFIX = "GoodsPromotionHandler-";

	GetGoodListResponse getGoodList(GetGoodListCommand cmd, ServiceModuleApp app);
	
}
