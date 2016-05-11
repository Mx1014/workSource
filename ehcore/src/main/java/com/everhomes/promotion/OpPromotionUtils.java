package com.everhomes.promotion;

import com.everhomes.bootstrap.PlatformContext;
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
            
        }
        
        return null;
    }
    
    public static OpPromotionAction getActionFromPromotion(OpPromotionActivity promotion) {
        return null;
    }
}
