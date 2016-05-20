package com.everhomes.promotion;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;

@Component
@Scope("prototype")
public class OpPromotionNewUserCondition implements OpPromotionCondition, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpPromotionNewUserCondition.class);
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private PromotionService promotionService;
    
    @Autowired 
    private GroupProvider groupProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private PromotionUserService promotionUserService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    private OpPromotionActivity cacheActivity; 
    
    @Override
    public void createCondition(OpPromotionContext ctx) {
        OpPromotionActivityContext c = (OpPromotionActivityContext) ctx;
        this.cacheActivity = c.getPromotion();
        
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhGroupMembers.class, null), this);
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, OrganizationMember.class, null), this);
        
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhOpPromotionActivities.class, this.cacheActivity.getId()), this);
    }

    private void unsubcribeMyself() {
        localBus.unsubscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhGroupMembers.class, null), this);
        localBus.unsubscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, OrganizationMember.class, null), this);
        localBus.unsubscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhOpPromotionActivities.class, this.cacheActivity.getId()), this);
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        try {
            Date now = DateHelper.currentGMTTime();
            Date endTime = this.cacheActivity.getEndTime();
            if(endTime.before(now)) {
                this.unsubcribeMyself();
                LOGGER.info("listen timeout, delete myself");
                return Action.none;
            }
            
            if(arg1.indexOf("EhOpPromotionActivities") >= 0) {
                this.unsubcribeMyself();
            } else if(arg1.indexOf("OrganizationMember") >= 0) {
                Long memberId = (Long)arg2;
                
                OrganizationMember member = organizationProvider.findOrganizationMemberById(memberId);
                OrganizationMemberTargetType targetType = OrganizationMemberTargetType.fromCode(member.getTargetType());
                if(targetType != OrganizationMemberTargetType.USER) {
                    return Action.none;
                }
                
                if(promotionUserService.checkOrganizationMember(this.cacheActivity, member)) {
                    OpPromotionAction action = OpPromotionUtils.getActionFromPromotion(this.cacheActivity);
                    
                    OpPromotionActivityContext ctx = new OpPromotionActivityContext(this.cacheActivity);
                    User u = userProvider.findUserById(member.getTargetId());
                    if(u != null) {
                        ctx.setUser(u);
                        ctx.setNeedUpdate(true);
                        action.fire(ctx);
                    }
                }
                    
            } else {
                Long groupMemberId = (Long)arg2;
                GroupMember groupMember = this.groupProvider.findGroupMemberById(groupMemberId);
                if(null == groupMember) {
                    LOGGER.error("None of groupMember");
                    return Action.none;
                    }
                
                if(groupMember.getMemberStatus() != GroupMemberStatus.ACTIVE.getCode()) {
                    return Action.none;
                    }
                
                OpPromotionAction action = OpPromotionUtils.getActionFromPromotion(this.cacheActivity);
                
                OpPromotionActivityContext ctx = new OpPromotionActivityContext(this.cacheActivity);
                User u = userProvider.findUserById(groupMember.getMemberId());
                if(u != null) {
                    ctx.setUser(u);
                    ctx.setNeedUpdate(true);
                    action.fire(ctx);
                }
            }

        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
            }
        
        return Action.none;
    }

}
