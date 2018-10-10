package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GoodBizEnum;
import com.everhomes.rest.goods.GoodTagDTO;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class PrintGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{

	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		//打印机所有域空间的商品都是一样的 这里直接使用meiju
		GoodBizEnum[] goodBizEnums = GoodBizEnum.values();
		List<GoodTagDTO> goods = new ArrayList<>();
		for (int i = 0 ; i < goodBizEnums.length; i++) {
			if (GoodBizEnum.TYPE_SIYIN_PRINT.equals(goodBizEnums[i].getType())) {
				GoodTagDTO good = new GoodTagDTO();
				good.setGoodTagKey(goodBizEnums[i].getIdentity());
				good.setGoodTagKey(goodBizEnums[i].getName());
				goods.add(good);
			}
		}
		GetGoodListResponse resp = new GetGoodListResponse();
		resp .setGoods(goods);
		return resp;
	}	
}
