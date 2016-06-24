package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.promotion.GetOpPromotionActivityByPromotionId;
import com.everhomes.rest.promotion.OpPromotionDTO;
import com.everhomes.user.UserContext;

@RestDoc(value="Promotion controller", site="core")
@RestController
@RequestMapping("/promotion")
public class PromotionController extends ControllerBase {
    @Autowired
    PromotionService promotionService;
    
    @Autowired
    private OpPromotionActivityProvider promotionActivityProvider;
    
    @RequestMapping("test2")
    @RestReturn(value=OpPromotionDTO.class)
    public RestResponse test2() {
        //promotionService.bizFetchCoupon(UserContext.current().getUser().getId(), 14636581126908022l);
        
//        DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, 2101341l);
        
        
        OpPromotionActivity act = promotionActivityProvider.getOpPromotionActivityById(69l);
        OpPromotionCondition cond = OpPromotionUtils.getConditionFromPromotion(act);
        
        OpPromotionActivityContext ctx = new OpPromotionActivityContext(act);
        cond.createCondition(ctx);;
        
        return new RestResponse();
    }
}
