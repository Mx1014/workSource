package com.everhomes.promotion.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.promotion.PromotionService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.promotion.CreateOpPromotionCommand;
import com.everhomes.rest.promotion.GetOpPromotionActivityByPromotionId;
import com.everhomes.rest.promotion.ListOpPromotionActivityResponse;
import com.everhomes.rest.promotion.ListPromotionCommand;
import com.everhomes.rest.promotion.OpPromotionActivityDTO;
import com.everhomes.rest.promotion.OpPromotionOrderRangeCommand;
import com.everhomes.rest.promotion.OpPromotionSearchCommand;
import com.everhomes.rest.promotion.UpdateOpPromotionCommand;

@RestDoc(value="Promotion Admin controller", site="core")
@RestController
@RequestMapping("/admin/promotion")
public class PromotionAdminController extends ControllerBase {
    
    @Autowired
    PromotionService promotionService;

    @RequestMapping("createPromotion")
    @RestReturn(value=OpPromotionActivityDTO.class)
    public RestResponse createPromoti(@Valid CreateOpPromotionCommand cmd) {
        promotionService.createPromotion(cmd);
        return new RestResponse();
    }
    
    @RequestMapping("listPromotion")
    @RestReturn(value=ListOpPromotionActivityResponse.class)
    public RestResponse listPromotion(@Valid ListPromotionCommand cmd) {
        return new RestResponse(promotionService.listPromotion(cmd));
    }    
    
    @RequestMapping("getPromotionById")
    @RestReturn(value=GetOpPromotionActivityByPromotionId.class)
    public RestResponse getPromotionById(@Valid GetOpPromotionActivityByPromotionId cmd) {
        return new RestResponse(promotionService.getPromotionById(cmd));
    }    
    
    @RequestMapping("closePromotion")
    @RestReturn(value=GetOpPromotionActivityByPromotionId.class)
    public RestResponse closePromotion(@Valid GetOpPromotionActivityByPromotionId cmd) {
        promotionService.closeOpPromotion(cmd);
        return new RestResponse();
    }
    
    @RequestMapping("newOrderPrice")
    @RestReturn(value=OpPromotionOrderRangeCommand.class)
    public RestResponse newOrderPrice(@Valid OpPromotionOrderRangeCommand cmd) {
        promotionService.newOrderPriceEvent(cmd);
        return new RestResponse("ok");
    }    

    @RequestMapping("searchPromotion")
    @RestReturn(value=ListOpPromotionActivityResponse.class)
    public RestResponse searchPromotion(@Valid OpPromotionSearchCommand cmd) {
        return new RestResponse(promotionService.searchPromotion(cmd));
    }

    @RequestMapping("updatePromotion")
    @RestReturn(value=String.class)
    public RestResponse udpatePromotion(@Valid UpdateOpPromotionCommand cmd) {
        promotionService.udpateOpPromotion(cmd);
        return new RestResponse("ok");
    } 
}
