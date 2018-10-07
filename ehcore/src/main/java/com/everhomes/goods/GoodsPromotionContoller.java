package com.everhomes.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.goods.GetGoodListCommand;
import com.everhomes.rest.promotion.order.GoodDTO;

@RestController
@RequestMapping("/goods")
public class GoodsPromotionContoller  extends ControllerBase {
	
	@Autowired
	GoodsService goodsServce;
	
	 @RequestMapping("getGoodList")
	 @RestReturn(value=GoodDTO.class,collection = true)
	 public RestResponse getGoodList(GetGoodListCommand cmd) {
		 List<GoodDTO> dtos = goodsServce.getGoodList(cmd);
	     RestResponse response = new RestResponse(dtos);
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
}
