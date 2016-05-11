package com.everhomes.promotion;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OpPromotionOrderValueCondition implements OpPromotionCondition {

    @Override
    public void createCondition(OpPromotionContext ctx) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteCondition(OpPromotionContext ctx) {
        // TODO Auto-generated method stub
        
    }

}
