package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + "printOrder") //要与OrderTypeEnum一致
public class PrintGoodsPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl{
	

//	@Override
//	public List<GoodDTO> getGoodList(GetGoodListCommand cmd, ServiceModuleApp app) {
//		
//		//根据商户id获取应用范围
//		List<GoodDTO> goods = getGoods(cmd, app);
//		List<GoodDTO> parents = getParents(goods);
//		return goods;
//	}
//
//	private List<GoodDTO> getParents(List<GoodDTO> goods) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private List<GoodDTO> getGoods(GetGoodListCommand cmd, ServiceModuleApp app) {
//		
//		List<GoodDTO> goods = new ArrayList<>();
////		goods
//		
//		return null;
//	}


	
}
