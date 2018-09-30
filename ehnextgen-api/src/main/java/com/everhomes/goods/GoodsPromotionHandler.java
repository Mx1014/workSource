package com.everhomes.goods;

import java.util.List;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

public interface GoodsPromotionHandler {

	String GOODS_PROMOTION_HANDLER_PREFIX = "GoodsPromotionHandler-";

	List<GoodDTO> getGoodList(GetGoodListCommand cmd, ServiceModuleApp app);
	
}
