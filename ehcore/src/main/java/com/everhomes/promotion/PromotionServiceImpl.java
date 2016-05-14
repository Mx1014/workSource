package com.everhomes.promotion;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.messaging.AddressMessageRoutingHandler;
import com.everhomes.rest.promotion.CreateOpPromotionCommand;
import com.everhomes.rest.promotion.OpPromotionAssignedScopeDTO;
import com.everhomes.rest.promotion.ScheduleTaskResourceType;
import com.everhomes.rest.promotion.ScheduleTaskStatus;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.EhOpPromotionActivities;
import com.everhomes.util.ConvertHelper;

@Component
public class PromotionServiceImpl implements PromotionService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressMessageRoutingHandler.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private BusBridgeProvider busBridgeProvider;
    
    @Autowired
    private OpPromotionActivityProvider promotionActivityProvider;
    
    @Autowired
    private OpPromotionAssignedScopeProvider promotionAssignedScopeProvider;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private ScheduleTaskProvider scheduleTaskProvider;
    
    @PostConstruct
    void setup() {
        String subcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhOpPromotionActivities.class, null);
        localBus.subscribe(subcribeKey, this);
        //localBus.unsubscribe(arg0, arg1);
     }
    
    @Override
    public void createPromotion(CreateOpPromotionCommand cmd) {
        //DaoHelper.publishDaoAction(DaoAction.CREATE, EhOpPromotionActivities.class, 5l);
        //localBus.publish("global", EhOpPromotionActivities.class.getName(), 5l);
        
        List<OpPromotionAssignedScopeDTO> scopes = cmd.getAssignedScopes();
        cmd.setAssignedScopes(null);
        
        OpPromotionActivity promotion = this.dbProvider.execute(new TransactionCallback<OpPromotionActivity>() {
            @Override
            public OpPromotionActivity doInTransaction(TransactionStatus arg0) {
                OpPromotionActivity activity = (OpPromotionActivity)ConvertHelper.convert(cmd, OpPromotionActivity.class);
                activity.setStartTime(new Timestamp(cmd.getStartTime()));
                activity.setEndTime(new Timestamp(cmd.getEndTime()));
                promotionActivityProvider.createOpPromotionActivitie(activity);
                
                for(OpPromotionAssignedScopeDTO dto : scopes) {
                    OpPromotionAssignedScope scope = (OpPromotionAssignedScope)ConvertHelper.convert(dto, OpPromotionAssignedScope.class);
                    scope.setPromotionId(activity.getId());
                    promotionAssignedScopeProvider.createOpPromotionAssignedScope(scope);
                    }
                
                return activity;
            }
        });
        
        String triggerName = "oppromotion-cron-" + System.currentTimeMillis();
        String jobName = triggerName;
        
        Map<String, Object> map = new HashMap<String, Object>();
        //String cronExpression = "0/5 * * * * ?";
        map.put("id", promotion.getId().toString());
        map.put("type", "start");
        //scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, OpPromotionScheduleJob.class, map);
        
        scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getStartTime().getTime()), OpPromotionScheduleJob.class, map);
        
        triggerName = "oppromotion-cron-" + System.currentTimeMillis();
        jobName = triggerName;
        Map<String, Object> map2 = new HashMap<String, Object>();
        map.put("id", promotion.getId().toString());
        map.put("type", "end");
        scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getEndTime().getTime()), OpPromotionScheduleJob.class, map2);
        
        //this.broadcastEvent(DaoAction.CREATE, EhOpPromotionActivities.class, promotion.getId());
    }
    
    @Override
    public void broadcastEvent(DaoAction action, Class<?> pojoClz, Long id) {
        LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
        
        String arg3 = "" + action.getName() + "." + pojoClz.getSimpleName();
        String arg1 = arg3 + "." + id.toString();
        
        //broadcast
        localBusSubscriber.onLocalBusMessage(null, arg1, id, arg3);
        
        DaoHelper.publishDaoAction(action, pojoClz, id);
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        //Must be
        try {
            Long id = (Long)arg2;
            if(null == id) {
                LOGGER.error("None of groupMember");
                return Action.none;
            } else {
                LOGGER.error(" id= " + id);
                }
        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
            }

        return Action.none;
    }
    
    @Override
    public ScheduleTask createScheduleTaskByPromotion(OpPromotionActivity promotion) {
        ScheduleTask task = new ScheduleTask();
        task.setNamespaceId(promotion.getNamespaceId());
        task.setProcessCount(0);
        task.setProgress(0);
        task.setResourceId(promotion.getId());
        task.setResourceType(ScheduleTaskResourceType.PROMOTION_ACTIVITY.getCode());
        task.setStatus(ScheduleTaskStatus.CREATING.getCode());
        scheduleTaskProvider.createScheduleTask(task);
        
        OpPromotionCondition condition = OpPromotionUtils.getConditionFromPromotion(promotion);
        OpPromotionActivityContext ctx = new OpPromotionActivityContext(promotion);
        condition.createCondition(ctx);
        
        return task;
    }
}
