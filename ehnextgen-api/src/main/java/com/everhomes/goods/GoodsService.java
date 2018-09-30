package com.everhomes.goods;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;

public interface GoodsService {
	
	GetGoodListResponse getGoodList(GetGoodListCommand cmd);
	GoodsPromotionHandler getGoodsPromotionHandler(Long moduleId);
}
