package com.everhomes.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJobTest implements ScheduleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map<String, Object> map = new HashMap<String, Object>();
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        Iterator<Entry<String, Object>> iterator = jobMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            map.put(entry.getKey(), entry.getValue());
        }
        LOGGER.info("Execute at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ", map=" + map);
    }

}
