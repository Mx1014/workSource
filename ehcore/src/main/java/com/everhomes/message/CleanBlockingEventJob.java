package com.everhomes.message;

import com.everhomes.util.DateHelper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CleanBlockingEventJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        ConcurrentHashMap stored = (ConcurrentHashMap)jobMap.get("stored");
        Iterator iter = stored.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if(entry.getKey().toString().contains(".expireTime") && Long.valueOf(entry.getValue().toString()) < DateHelper.currentGMTTime().getTime()){
                iter.remove();
            }
        }
    }
}
