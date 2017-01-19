package com.everhomes.promotion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.rest.promotion.OpPromotionScopeType;
import com.everhomes.user.User;


@Component
@Scope("prototype")
public class OpPromotionListAllCondition implements OpPromotionCondition, OpPromotionUserCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpPromotionListAllCondition.class);
    
    @Autowired
    PromotionUserService promotionUserService;
    
    @Autowired
    PromotionService promotionService;
    
    @Autowired
    private OpPromotionAssignedScopeProvider promotionAssignedScopeProvider;
    
    @Override
    public void createCondition(OpPromotionContext ctx) {
        try {
            OpPromotionUserVisitor visitor = new OpPromotionUserVisitor();
            
            //TODO use OpPromotionActivityContext directly
            OpPromotionActivityContext c = (OpPromotionActivityContext) ctx;
            visitor.setPromotion(c.getPromotion());
            List<OpPromotionAssignedScope> scopes = promotionAssignedScopeProvider.getOpPromotionScopeByPromotionId(c.getPromotion().getId());
            for(OpPromotionAssignedScope scope : scopes) {
                OpPromotionScopeType scopeType = OpPromotionScopeType.fromCode(scope.getScopeCode());
                switch(scopeType) {
                case ALL:
                    promotionUserService.listAllUser(visitor, this);
                    break;
                case COMMUNITY:
                    visitor.setValue(scope.getScopeId());
                    promotionUserService.listUserByCommunity(visitor, this);
                    break;
                case CITY:
                    visitor.setValue(scope.getScopeId());
                    promotionUserService.listUserByCity(visitor, this);
                    break;
                case ORGANIZATION:
                    visitor.setValue(scope.getScopeId());
                    promotionUserService.listUserByCompany(visitor, this);
                    break;
                case USER:
                    visitor.setValue(scope.getScopeId());
                    promotionUserService.listUserByUserId(visitor, this);
                default:
                    LOGGER.error("scopeType not found");
                    break;
                }
            }
            
            if(visitor.getPushCount() > 0) {
                promotionService.addPushCountByPromotionId(visitor.getPromotion().getId(), (int)visitor.getPushCount());
                visitor.setPushCount(0);
            }            
            promotionService.finishOpPromotion(c.getPromotion());
            
            LOGGER.info("finished promotion id=" + c.getPromotion().getId());
        } catch(Exception ex) {
            LOGGER.error("promotion list all error", ex);
        }

    }

//    @Override
//    public void deleteCondition(OpPromotionContext ctx) {
//    }

    @Override
    public void userFound(User u, OpPromotionUserVisitor visitor) {
        OpPromotionAction action = OpPromotionUtils.getActionFromPromotion(visitor.getPromotion());
        
        OpPromotionActivityContext ctx = new OpPromotionActivityContext(visitor.getPromotion());
        ctx.setUser(u);
        
        visitor.setPushCount(visitor.getPushCount()+1);
        
        action.fire(ctx);
        
        if(visitor.getPushCount() % 100 == 0) {
            promotionService.addPushCountByPromotionId(visitor.getPromotion().getId(), (int)visitor.getPushCount());
            visitor.setPushCount(0);
            
            //TODO better 目前简单粗暴的方法，sleep 方式来减少 cpu 占用。
            try {
            	Thread.sleep(10*1000L);
				} catch (Exception e) {
					LOGGER.error("sleep error", e);
				}
            
        }
    }

}
