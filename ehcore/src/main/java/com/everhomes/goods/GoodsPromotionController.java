package com.everhomes.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.goods.*;
import com.everhomes.rest.promotion.order.GoodDTO;

@RestController
@RequestMapping("/goods")
public class GoodsPromotionController  extends ControllerBase {
	
	@Autowired
	GoodsService goodsServce;
	
	 @RequestMapping("getGoodList")
	 @RestReturn(value=GetGoodListResponse.class)
	 public RestResponse getGoodList(GetGoodListCommand cmd) {
		 GetGoodListResponse response = goodsServce.getGoodList(cmd);
		 RestResponse resp = new RestResponse(response);
	     resp.setErrorCode(ErrorCodes.SUCCESS);
	     resp.setErrorDescription("OK");
	     return resp;
	 }
	 
	 
	 @RequestMapping("getServiceGoods")
	 @RestReturn(value=GetServiceGoodResponse.class)
	 public RestResponse getServiceGoods(GetServiceGoodCommand cmd) {
		 GetServiceGoodResponse dtos = goodsServce.getServiceGoodList(cmd);
	     RestResponse response = new RestResponse(dtos);
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
}
