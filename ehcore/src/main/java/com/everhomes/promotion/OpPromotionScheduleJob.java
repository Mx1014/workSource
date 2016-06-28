package com.everhomes.promotion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.rest.promotion.OpPromotionConditionType;
import com.everhomes.rest.promotion.OpPromotionStatus;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;

@Component
@Scope("prototype")
public class OpPromotionScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
    
    @Autowired
    private OpPromotionActivityProvider promotionActivityProvider;
    
    @Autowired
    private PromotionService promotionService;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        
        String idStr = (String)jobMap.get("id");
        Long id = Long.parseLong(idStr);
        
        OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivityById(id);
        if(promotion == null) {
            LOGGER.error("promotion not found in schdule job");
            return;
        }
        
        if(jobMap.get(OpPromotionConstant.SCHEDULE_TYPE).equals(OpPromotionConstant.SCHEDULE_START)) {
            
            if(promotion.getStatus().equals(OpPromotionStatus.INACTIVE.getCode())) {
                LOGGER.info("OpPromotion already closed");
                return;
            }
            
            OpPromotionConditionType conditionType = OpPromotionConditionType.fromCode(promotion.getPolicyType());
            switch(conditionType) {
            case NEW_USER:
                //broadcast to all nodes
                promotionService.broadcastEvent(DaoAction.CREATE, EhOpPromotionActivities.class, id);
                break;
            default:
                //Local only
                DaoHelper.publishDaoAction(DaoAction.CREATE, EhOpPromotionActivities.class, id);
            }
            
            LOGGER.info("broadcast to all, OpPromotion schedule Id=" + id);
            
            Map<String, Object> map = new HashMap<String, Object>();
            String triggerName = OpPromotionConstant.SCHEDULE_TARGET_NAME + System.currentTimeMillis();
            String jobName = triggerName;
            map.put("id", promotion.getId().toString());
            map.put(OpPromotionConstant.SCHEDULE_TYPE, OpPromotionConstant.SCHEDULE_END);
            scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getEndTime().getTime()), OpPromotionScheduleJob.class, map);            
        } else {

            LOGGER.info("close OpPromotion schedule Id=" + id);
            promotionService.closeOpPromotion(promotion);
        }
        

    }
}
