package com.everhomes.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.goods.GetGoodListResponse;

@RestController
@RequestMapping("/goods")
public class GoodsPromotionContoller  extends ControllerBase {
	
	@Autowired
	GoodsService goodsServce;
	
	 @RequestMapping("/getGoodList")
	 @RestReturn(value=GetGoodListResponse.class)
	 public RestResponse getGoodList(GetGoodListCommand cmd) {
	     RestResponse response = new RestResponse(goodsServce.getGoodList(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
}
