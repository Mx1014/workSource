// @formatter:off
package com.everhomes.scheduler;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.scheduler.ScheduleServiceErrorCode;
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

    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleAtTime(String triggerName, String jobName, Date startTime, Class jobClass, Map<String, Object> parameters) {
        if(triggerName == null || triggerName.length() == 0) {
            LOGGER.error("The trigger name may not be empty, triggerName={}", triggerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The trigger name may not be empty");
        }
        
        if(jobName == null || jobName.length() == 0) {
            LOGGER.error("The job name may not be empty, jobName={}", jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job name may not be empty");
        }
        
        if(startTime == null) {
            LOGGER.error("The start time may not be null, triggerName={}, jobName={}", triggerName, jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The start time may not be null");
        }

        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(startTime);
        if(jobClass == null) {
            LOGGER.error("The job class may not be null, triggerName={}, jobName={}, groupName={}, startTime={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job class may not be null");
        }
        
        try {
            JobDetail job = newJob(jobClass).withIdentity(jobName, DEFAULT_GROUP).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, DEFAULT_GROUP).startAt(startTime).build();
            
            if(parameters != null) {
                job.getJobDataMap().putAll(parameters);
            }
            scheduler.scheduleJob(job, trigger);
            scheduler.scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(repeat), triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}, parameters={}", 
                    triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName());
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName());
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleWithRepeat(String triggerName, String jobName, Date startTime, long msInterval, int repeatCount, 
        Class jobClass, Map<String, Object> parameters) {
        if(triggerName == null || triggerName.length() == 0) {
            LOGGER.error("The trigger name may not be empty, triggerName={}", triggerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The trigger name may not be empty");
        }
        
        if(jobName == null || jobName.length() == 0) {
            LOGGER.error("The job name may not be empty, jobName={}", jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job name may not be empty");
        }
        
        if(startTime == null) {
            LOGGER.error("The start time may not be null, triggerName={}, jobName={}", triggerName, jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The start time may not be null");
        }

        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(startTime);
        if(jobClass == null) {
            LOGGER.error("The job class may not be null, triggerName={}, jobName={}, groupName={}, startTime={}", 
                triggerName, jobName, startTimeStr);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job class may not be null");
        }
        
        if(msInterval < 0) {
            LOGGER.error("The interval may not be less than 0, triggerName={}, jobName={}, interval={}", triggerName, jobName, msInterval);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The start time may not be null");
        }
        
        try {
            JobDetail job = newJob(jobClass).withIdentity(jobName, DEFAULT_GROUP).build();
            SimpleScheduleBuilder builder = null;
            if(repeatCount <= 0) {
                builder = simpleSchedule().withIntervalInMilliseconds(msInterval).repeatForever();
            } else {
                builder = simpleSchedule().withIntervalInMilliseconds(msInterval).withRepeatCount(repeatCount);
            }
            Trigger trigger = newTrigger().withIdentity(triggerName, DEFAULT_GROUP).startAt(startTime)
                .withSchedule(builder).build();
            
            if(parameters != null) {
                job.getJobDataMap().putAll(parameters);
            }
            scheduler.scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(repeat), triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleWithCron(String triggerName, String jobName, String cronExpression, Class jobClass, Map<String, Object> parameters) {
        if(triggerName == null || triggerName.length() == 0) {
            LOGGER.error("The trigger name may not be empty, triggerName={}", triggerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The trigger name may not be empty");
        }
        
        if(jobName == null || jobName.length() == 0) {
            LOGGER.error("The job name may not be empty, jobName={}", jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The job name may not be empty");
        }
        
        if(cronExpression == null || cronExpression.length() == 0) {
            LOGGER.error("The cron expression may not be empty, triggerName={}, jobName={}", triggerName, jobName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "The cron expression may not be null");
        }
        
        try {
            JobDetail job = newJob(jobClass).withIdentity(triggerName, DEFAULT_GROUP).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, DEFAULT_GROUP).withSchedule(cronSchedule(cronExpression)).build();
            
            if(parameters != null) {
                job.getJobDataMap().putAll(parameters);
            }
            
            scheduler.scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(cron), triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    public boolean checkExist(String triggerName, String jobName) {
        try {
            boolean isJobExist = scheduler.checkExists(JobKey.jobKey(jobName, DEFAULT_GROUP));
            boolean isTriggerExist = scheduler.checkExists(TriggerKey.triggerKey(triggerName, DEFAULT_GROUP));
            if(isJobExist && isTriggerExist) {
                return true;
            }
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Check the job whether existed, triggerName={}, jobName={}, groupName={}, isTriggerExist={}, isJobExist={}", 
                    triggerName, jobName, DEFAULT_GROUP, isTriggerExist, isJobExist);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to check the job whether existed, triggerName={}, jobName={}, groupName={}", 
                triggerName, jobName, DEFAULT_GROUP);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_CHECK_JOB_FAILED, "Failed to check the job whether existed");
        }
        
        return false;
    }
    
    @Override
    public boolean unscheduleJob(String triggerName) {
        boolean result = false;
        try {
            result = scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, DEFAULT_GROUP));
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Unschedule the job, triggerName={}, groupName={}, result={}", triggerName, DEFAULT_GROUP, result);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to unschedule the job, triggerName={}, groupName={}", 
                triggerName, DEFAULT_GROUP);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_UNSCHEDULE_JOB_FAILED, "Failed to unschedule the job");
        }
        
        return result;
    }
}
