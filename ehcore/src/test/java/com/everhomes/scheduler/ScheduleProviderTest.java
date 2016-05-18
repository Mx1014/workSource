// @formatter:off
package com.everhomes.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.base.LoginAuthTestCase;

public class ScheduleProviderTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
    
	/** fdfasdf  */
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Before
    public void setUp() throws Exception {
    	super.setUp();
    }
    
    @After
    public void tearDown() {
    	cleanData();
    }
    
    @Test
    public void testScheduleAt() {
        String triggerName = "schedule-at-" + System.currentTimeMillis();
        String jobName = triggerName;
        long interval = 10 * 1000;
        Date startTime = new Date(System.currentTimeMillis() + interval);
        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startTime", "startTimeStr");
        scheduleProvider.scheduleSimpleJob(triggerName, jobName, startTime, SimpleJobTest.class, map);
        LOGGER.info("Schedule a job at " + startTimeStr + ", map=" + map);
        
        try {
            Thread.sleep(interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testScheduleWithCron() {
        String triggerName = "schedule-cron-" + System.currentTimeMillis();
        String jobName = triggerName;
        long interval = 60 * 1000;
        Date startTime = new Date(System.currentTimeMillis());
        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        Map<String, Object> map = new HashMap<String, Object>();
        String cronExpression = "0/5 * * * * ?";
        map.put("cronExpression", cronExpression);
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, SimpleJobTest.class, map);
        LOGGER.info("Schedule a job with " + cronExpression + " at " + startTimeStr + ", map=" + map);
        
        try {
            Thread.sleep(interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testListJobInfo() {
        String triggerName = "schedule-at-" + System.currentTimeMillis();
        String jobName = triggerName;
        long interval = 20 * 1000;
        Date startTime = new Date(System.currentTimeMillis() + interval);
        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startTime", "startTimeStr");
        scheduleProvider.scheduleSimpleJob(triggerName, jobName, startTime, SimpleJobTest.class, map);
        LOGGER.info("Schedule a job at " + startTimeStr + ", map=" + map);
        
        triggerName = "schedule-cron-" + System.currentTimeMillis();
        jobName = triggerName;
        String cronExpression = "0/5 * * * * ?";
        map = new HashMap<String, Object>();
        map.put("cronExpression", cronExpression);
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, SimpleJobTest.class, map);
        LOGGER.info("Schedule a job with " + cronExpression + " at " + startTimeStr + ", map=" + map);
        
        triggerName = "schedule-repeat-" + System.currentTimeMillis();
        jobName = triggerName;
        long msInterval = 10 * 1000;
        int repeatCount = 5;
        map = new HashMap<String, Object>();
        map.put("msInterval", msInterval);
        map.put("repeatCount", repeatCount);
        scheduleProvider.scheduleRepeatJob(triggerName, jobName, startTime, msInterval, repeatCount, SimpleJobTest.class, map);
        
        try {
            Thread.sleep(10 * 1000);
            List<ScheduleJobInfoDTO> jobInfoList = scheduleProvider.listScheduleJobs();
            for(ScheduleJobInfoDTO jobInfo : jobInfoList) {
                LOGGER.info("Schedule a job(job info), jobInfo=" + jobInfo);
            }
            
            Thread.sleep(50 * 1000);
            jobInfoList = scheduleProvider.listScheduleJobs();
            for(ScheduleJobInfoDTO jobInfo : jobInfoList) {
                LOGGER.info("Schedule a job(job info), jobInfo=" + jobInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cleanData() {
    }
}
