package com.everhomes.goods;

import java.util.List;

import com.everhomes.rest.goods.FindGoodCommand;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GetServiceGoodCommand;
import com.everhomes.rest.goods.GetServiceGoodResponse;
import com.everhomes.rest.promotion.order.GoodDTO;

public interface GoodsPromotionHandler {

	String GOODS_PROMOTION_HANDLER_PREFIX = "GoodsPromotionHandler-";

	GetGoodListResponse getGoodList(GetGoodListCommand cmd);
	
	GoodDTO findGoodByTags(FindGoodCommand cmd);

	GetServiceGoodResponse getServiceGoodsScopes(GetServiceGoodCommand cmd);
}
