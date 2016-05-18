package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.promotion.OpPromotionDTO;

@RestDoc(value="Promotion controller", site="core")
@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    
    @RequestMapping("test2")
    @RestReturn(value=OpPromotionDTO.class)
    public RestResponse test2() {
        return new RestResponse();
    }
}
