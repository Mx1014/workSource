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
		GoodDTO dto = new GoodDTO();
		dto.setCounts(1);
		dto.setGoodDescription("testDescription");
		dto.setGoodName("商品");
		dto.setId(100L);
		dto.setPrice(new BigDecimal("2.1"));
		dto.setTotalPrice(new BigDecimal("2.2"));
		dto.setTag1("print");
		dto.setTag2("copy");
		return Arrays.asList(dto);
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
