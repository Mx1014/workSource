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
import com.everhomes.rest.promotion.OpPromotionActivityDTO;

@RestDoc(value="Promotion Admin controller", site="core")
@RestController
@RequestMapping("/admin/promotion")
public class PromotionAdminController extends ControllerBase {
    
    @Autowired
    PromotionService promotionService;

    @RequestMapping("createPromotion")
    @RestReturn(value=OpPromotionActivityDTO.class)
    public RestResponse createPromotion(@Valid CreateOpPromotionCommand cmd) {
        promotionService.createPromotion(cmd);
        return new RestResponse();
    }
}
