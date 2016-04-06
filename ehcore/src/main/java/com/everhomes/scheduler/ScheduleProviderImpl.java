// @formatter:off
package com.everhomes.scheduler;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.forum.ForumServiceErrorCode;
import com.everhomes.rest.scheduler.ScheduleServiceErrorCode;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ScheduleProviderImpl implements ScheduleProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");

	@Autowired
	private DbProvider dbProvider;
	
	/** 调度器 */
	private Scheduler scheduler;

    @PostConstruct
	protected void initialize() throws Exception {
        long startTime = System.currentTimeMillis();
        
	    SchedulerFactory sf = new StdSchedulerFactory();
	    scheduler = sf.getScheduler();
	    scheduler.start();
	    
	    long endTime = System.currentTimeMillis();
	    if(LOGGER.isInfoEnabled()) {
	        LOGGER.info("Scheduler startd, elapse={}", (endTime - startTime));
	    }
	}

    public void startAt(String identifier, Date startTime, Class<ScheduleJob> jobClass) {
        if(identifier == null || identifier.length() == 0) {
            LOGGER.error("The identifier may not be empty, identifier={}", identifier);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The identifier may not be empty");
        }
        
        if(startTime == null) {
            LOGGER.error("The start time may not be null, identifier={}", identifier);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The start time may not be null");
        }

        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(startTime);
        if(jobClass == null) {
            LOGGER.error("The job class may not be null, identifier={}, groupName={}, startTime={}", identifier, startTimeStr);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job class may not be null");
        }
        
        JobDetail job = newJob(jobClass).withIdentity(identifier, DEFAULT_GROUP).build();
        Trigger trigger = newTrigger().withIdentity(identifier, DEFAULT_GROUP).startAt(startTime).build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, identifier={}, groupName={}, startTime={}, jobClass={}", 
                identifier, startTimeStr, jobClass.getName());
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
}
