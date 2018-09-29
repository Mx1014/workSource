package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.rest.goods.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class PrintGoodsPromotionHandlerImpl implements GoodsPromotionHandler{
	

	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd, ServiceModuleApp app) {
		
		//根据商户id获取应用范围
		List<GoodDTO> goods = getGoods(cmd, app);
		List<GoodDTO> parents = getParents(goods);
		GetGoodListResponse resp = new GetGoodListResponse();
		resp.setParents(parents);
		resp.setGoods(goods);
		return resp;
	}

	private List<GoodDTO> getParents(List<GoodDTO> goods) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<GoodDTO> getGoods(GetGoodListCommand cmd, ServiceModuleApp app) {
		
		List<GoodDTO> goods = new ArrayList<>();
//		goods
		
		
		return null;
	}


	
}
