package com.everhomes.goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

public class DefaultGoodsPromotionHandlerImpl implements GoodsPromotionHandler{
//	OrderTypeEnum

	@Override
	public List<GoodDTO> getGoodList(GetGoodListCommand cmd, ServiceModuleApp app) {
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

	private Map<String, String> getProvider(GetGoodListCommand cmd, ServiceModuleApp app) {
		List<Map<String, String>> tags = getGoodTags(cmd, app);
		
//		String providerKey = ;
//		String providerName = ;
		
		return null;
	}

	private List<Map<String, String>> getGoodTags(GetGoodListCommand cmd, ServiceModuleApp app) {
		return null;
	}

	private List<GoodDTO> getParents(List<GoodDTO> goods) {
		return null;
	}

	private List<GoodDTO> getGoods(GetGoodListCommand cmd, ServiceModuleApp app) {
		List<GoodDTO> goods = new ArrayList<>();
		return null;
	}


	
}
