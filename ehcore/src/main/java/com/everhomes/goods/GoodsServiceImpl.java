package com.everhomes.goods;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;

@Component
public class GoodsServiceImpl implements GoodsService{
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);
	
	@Autowired
	ServiceModuleAppService serviceModuleAppService;

	@Override
	public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
		
		ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(cmd.getAppOriginId());
		if (null == app) {
			return new GetGoodListResponse();
		}
		
		GoodsPromotionHandler handler = getGoodsPromotionHandler(app.getModuleId());
		return handler.getGoodList(cmd, app);
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

}
