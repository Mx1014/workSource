package com.everhomes.goods;

import java.util.List;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.promotion.order.GoodDTO;

public interface GoodsService {
	
	List<GoodDTO> getGoodList(GetGoodListCommand cmd);
	GoodsPromotionHandler getGoodsPromotionHandler(Long moduleId);
}
