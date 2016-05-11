package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalBusSubscriber.Action;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;

@Component
@Scope("prototype")
public class OpPromotionNewUserCondition implements OpPromotionCondition, LocalBusSubscriber {
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private PromotionService promotionService;
    
    @Override
    public void createCondition(OpPromotionContext ctx) {        
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhGroupMembers.class, null), this);
    }

    @Override
    public void deleteCondition(OpPromotionContext ctx) {
        
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        //TODO create a job for this user
        
        return Action.none;
    }

}
