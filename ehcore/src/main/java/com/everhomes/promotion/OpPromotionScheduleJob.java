package com.everhomes.promotion;

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

@Component
@Scope("prototype")
public class OpPromotionScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
    
    @Autowired
    OpPromotionActivityProvider promotionActivityProvider;
    
    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        
        String idStr = (String)jobMap.get("id");
        Long id = Long.parseLong(idStr);
        
        OpPromotionActivity promotion = promotionActivityProvider.getOpPromotionActivitieById(id);
        
        OpPromotionCondition condition = OpPromotionUtils.getConditionFromPromotion(promotion);
        OpPromotionActivityContext ctx = new OpPromotionActivityContext(promotion);
        condition.createCondition(ctx);
        
        LOGGER.error("OpPromotion schedule Id=" + id);
    }
}
