package com.everhomes.goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.promotion.order.GoodDTO;

public class DefaultGoodsPromotionHandlerImpl implements GoodsPromotionHandler{

	@Override
	public List<GoodDTO> getGoodList(GetGoodListCommand cmd) {
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		good.setTag1("print");
		good.setTag2("copy");
		good.setServeApplyName("这里填写服务提供商名称"); //
		good.setGoodTag("这里填写商品标志");// 商品标志
		good.setGoodName("这里填写商品名称");// 商品名称
		good.setGoodDescription("这里填写商品描述");// 商品描述
//		good.setCounts(1);
//		good.setPrice(trueDecimalAmount);
		return Arrays.asList(good);
	}
	

}
