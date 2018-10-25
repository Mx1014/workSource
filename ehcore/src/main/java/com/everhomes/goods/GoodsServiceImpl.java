package com.everhomes.goods;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.print.SiyinPrintPrinter;
import com.everhomes.print.SiyinPrintPrinterProvider;
import com.everhomes.rest.goods.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class GoodsServiceImpl implements GoodsService{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		GoodsPromotionHandler handler = getGoodsPromotionHandler(cmd.getBizType());
		return handler.getGoodList(cmd);
	}
	
	@Override
	public GoodsPromotionHandler getGoodsPromotionHandler(Long moduleId) {
		
		GoodsPromotionHandler handler = null;

		if(moduleId != null && moduleId.longValue() > 0) {
			String handlerPrefix = GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX;
			try {
				handler = PlatformContextNoWarnning.getComponent(handlerPrefix + moduleId);
			}catch (Exception ex){
				LOGGER.error("getGoodsPromotionHandler not exist moduleId = {}", moduleId);
			}
		}

		return handler;
	}

	@Override 
	public GetServiceGoodResponse getServiceGoodList(GetServiceGoodCommand cmd) {
		GoodsPromotionHandler handler = getGoodsPromotionHandler(cmd.getBizType());
		return handler.getServiceGoodsScopes(cmd);
	}
}
