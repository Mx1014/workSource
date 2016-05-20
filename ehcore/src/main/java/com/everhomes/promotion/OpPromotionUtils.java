package com.everhomes.promotion;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.promotion.OpPromotionActionType;
import com.everhomes.rest.promotion.OpPromotionConditionType;

public class OpPromotionUtils {
    public static OpPromotionCondition getConditionFromPromotion(OpPromotionActivity promotion) {
        //promotion.getActionType();
        
        OpPromotionConditionType condition = OpPromotionConditionType.fromCode(promotion.getPolicyType()); 
                
        switch(condition) {
        case ALL:
            return PlatformContext.getComponent(OpPromotionListAllCondition.class);
        case NEW_USER:
            return PlatformContext.getComponent(OpPromotionNewUserCondition.class);
        case ORDER_RANGE_VALUE:
            return PlatformContext.getComponent(OpPromotionOrderValueCondition.class);
        }
        
        return null;
    }
    
    public static OpPromotionAction getActionFromPromotion(OpPromotionActivity promotion) {
        OpPromotionActionType action = OpPromotionActionType.fromCode(promotion.getActionType());
        switch(action) {
        case STATIC_WEB_PAGE:
            return PlatformContext.getComponent(OpPromotionStaticWebPageAction.class);
        case COUPON:
            return PlatformContext.getComponent(OpPromotionCouponAction.class);
        case TEXT_ONLY:
            return PlatformContext.getComponent(OpPromotionTextOnlyAction.class);
        }
        
        return null;
    }
}
