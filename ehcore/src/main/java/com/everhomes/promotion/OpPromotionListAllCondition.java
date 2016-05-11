package com.everhomes.promotion;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class OpPromotionListAllCondition implements OpPromotionCondition {

    @Override
    public void createCondition(OpPromotionContext ctx) {
        // TODO Auto-generated method stub
        
        //select * from xxx
        /*for(a : users) {
         OpPromotionAction action = getActionFromContext(ctx);
         action.fire(ctx, a);
         }*/
    }

    @Override
    public void deleteCondition(OpPromotionContext ctx) {
        // TODO Auto-generated method stub
        
    }
    
    private OpPromotionAction getActionFromContext(OpPromotionContext ctx) {
        return null;
    }

}
