package com.everhomes.promotion;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OpPromotionOrderValueCondition implements OpPromotionCondition {

    @Override
    public void createCondition(OpPromotionContext ctx) {
    }

//    @Override
//    public void deleteCondition(OpPromotionContext ctx) {
//    }

}
