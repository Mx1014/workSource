package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.user.User;


@Component
@Scope("prototype")
public class OpPromotionListAllCondition implements OpPromotionCondition, OpPromotionUserCallback {
    @Autowired
    PromotionUserService promotionUserService;
    
    @Override
    public void createCondition(OpPromotionContext ctx) {
        OpPromotionUserVisitor visitor = new OpPromotionUserVisitor();
        
        //TODO use OpPromotionActivityContext directly
        OpPromotionActivityContext c = (OpPromotionActivityContext) ctx;
        visitor.setPromotion(c.getPromotion());
        
        promotionUserService.listAllUser(visitor, this);
    }

    @Override
    public void deleteCondition(OpPromotionContext ctx) {
        //nothing to delete
    }

    @Override
    public void userFound(User u, OpPromotionUserVisitor visitor) {
        OpPromotionAction action = OpPromotionUtils.getActionFromPromotion(visitor.getPromotion());
        
        OpPromotionActivityContext ctx = new OpPromotionActivityContext(visitor.getPromotion());
        ctx.setUser(u);
        
        action.fire(ctx);
    }

}
