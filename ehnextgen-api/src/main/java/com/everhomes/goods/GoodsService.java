package com.everhomes.goods;

import java.util.List;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GetServiceGoodCommand;
import com.everhomes.rest.goods.GetServiceGoodResponse;
import com.everhomes.rest.goods.GoodTagInfo;
import com.everhomes.rest.promotion.order.GoodDTO;

public interface GoodsService {
	
	GetGoodListResponse getGoodList(GetGoodListCommand cmd);
	GoodsPromotionHandler getGoodsPromotionHandler(Long moduleId);
	GetServiceGoodResponse getServiceGoodList(GetServiceGoodCommand cmd);
}
