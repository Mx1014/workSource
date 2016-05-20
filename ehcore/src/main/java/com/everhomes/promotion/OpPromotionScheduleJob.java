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

import com.everhomes.rest.promotion.OpPromotionStatus;
import com.everhomes.scheduler.ScheduleProvider;

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
            promotion = promotionActivityProvider.getOpPromotionActivityById(id);
            promotionService.createScheduleTaskByPromotion(promotion);
            
            LOGGER.error("OpPromotion schedule Id=" + id);
            
            Map<String, Object> map = new HashMap<String, Object>();
            String triggerName = OpPromotionConstant.SCHEDULE_TARGET_NAME + System.currentTimeMillis();
            String jobName = triggerName;
            map.put("id", promotion.getId().toString());
            map.put(OpPromotionConstant.SCHEDULE_TYPE, OpPromotionConstant.SCHEDULE_END);
            scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(promotion.getEndTime().getTime()), OpPromotionScheduleJob.class, map);            
        } else {

            //promotionService.closeOpPromotion(promotion);
            
//          OpPromotionCondition condition = OpPromotionUtils.getConditionFromPromotion(promotion);            
//            OpPromotionActivityContext ctx = new OpPromotionActivityContext(promotion); 
//            condition.deleteCondition(ctx);
        }
        

    }
}
