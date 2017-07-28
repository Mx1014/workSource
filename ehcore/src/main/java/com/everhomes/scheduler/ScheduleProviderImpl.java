// @formatter:off
package com.everhomes.scheduler;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.statistics.terminal.StatTerminalScheduleJob;
import com.everhomes.util.ExecutorUtil;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;
import com.everhomes.rest.scheduler.ScheduleServiceErrorCode;
import com.everhomes.util.RuntimeErrorException;

@Component
//public class ScheduleProviderImpl extends SchedulerFactoryBean implements ScheduleProvider {
public class ScheduleProviderImpl implements ScheduleProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");

	@Autowired
	private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;

    @Value("${schedule.running.flag}")
    private volatile byte localRunningFlag;

    private volatile byte runningFlag;

    private volatile long latestTime;

	/** 调度器
	 * @return */
	//private Scheduler scheduler;

//    @PostConstruct
//	protected void initialize() throws Exception {
//        long startTime = System.currentTimeMillis();
//        
//	    SchedulerFactory sf = new StdSchedulerFactory();
//	    scheduler = sf.getScheduler();
//	    scheduler.start();
//	    
//	    long endTime = System.currentTimeMillis();
//	    if(LOGGER.isInfoEnabled()) {
//	        LOGGER.info("Scheduler startd, elapse={}", (endTime - startTime));
//	    }
//	}
	
	private Scheduler getScheduler() {
	    return schedulerFactory.getScheduler();
	}

    @Override
    public void scheduleSimpleJob(String triggerName, String jobName, Date startTime, String jobClassName, Map<String, Object> parameters) {
        scheduleSimpleJob(triggerName, jobName, startTime, createJobClass(jobClassName), parameters);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleSimpleJob(String triggerName, String jobName, Date startTime, Class jobClass, Map<String, Object> parameters) {
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
            @SuppressWarnings("unchecked")
            JobDetail job = newJob(jobClass).withIdentity(jobName, DEFAULT_GROUP).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, DEFAULT_GROUP).startAt(startTime).build();
            
            if(parameters != null) {
                job.getJobDataMap().putAll(parameters);
            }
            getScheduler().scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(repeat), triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}, parameters={}", 
                    triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName(), e);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, startTime={}, jobClass={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, jobClass.getName(), e);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    public void scheduleRepeatJob(String triggerName, String jobName, Date startTime, long msInterval, int repeatCount, 
        String jobClassName, Map<String, Object> parameters) {
        scheduleRepeatJob(triggerName, jobName, startTime, msInterval, repeatCount, createJobClass(jobClassName), parameters);
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleRepeatJob(String triggerName, String jobName, Date startTime, long msInterval, int repeatCount, 
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
            @SuppressWarnings("unchecked")
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
            getScheduler().scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(repeat), triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters, e);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, startTime={}, msInterval={}, repeatCount={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, startTimeStr, msInterval, repeatCount, jobClass.getName(), parameters, e);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    public void scheduleCronJob(String triggerName, String jobName, String cronExpression, String jobClassName, Map<String, Object> parameters) {
        scheduleCronJob(triggerName, jobName, cronExpression, createJobClass(jobClassName), parameters);
    }


    @Override
    @SuppressWarnings("rawtypes")
    public void scheduleCronJob(String triggerName, String jobName, String cronExpression, Class jobClass, Map<String, Object> parameters) {
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
            @SuppressWarnings("unchecked")
            JobDetail job = newJob(jobClass).withIdentity(triggerName, DEFAULT_GROUP).build();
            Trigger trigger = newTrigger().withIdentity(triggerName, DEFAULT_GROUP).withSchedule(cronSchedule(cronExpression)).build();
            
            if(parameters != null) {
                job.getJobDataMap().putAll(parameters);
            }
            
            getScheduler().scheduleJob(job, trigger);
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Schedule a new job(cron), triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.error("The job is already existed, triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters, e);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to schedule the job, triggerName={}, jobName={}, groupName={}, cronExpression={}, jobClass={}, parameters={}", 
                triggerName, jobName, DEFAULT_GROUP, cronExpression, jobClass.getName(), parameters, e);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_SCHEDULE_JOB_FAILED, "Failed to schedule the job");
        }
    }
    
    @Override
    public boolean checkExist(String triggerName, String jobName) {
        try {
            boolean isJobExist = getScheduler().checkExists(JobKey.jobKey(jobName, DEFAULT_GROUP));
            boolean isTriggerExist = getScheduler().checkExists(TriggerKey.triggerKey(triggerName, DEFAULT_GROUP));
            if(isJobExist && isTriggerExist) {
                return true;
            }
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Check the job whether existed, triggerName={}, jobName={}, groupName={}, isTriggerExist={}, isJobExist={}", 
                    triggerName, jobName, DEFAULT_GROUP, isTriggerExist, isJobExist);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to check the job whether existed, triggerName={}, jobName={}, groupName={}", 
                triggerName, jobName, DEFAULT_GROUP, e);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_CHECK_JOB_FAILED, "Failed to check the job whether existed");
        }
        
        return false;
    }
    
    @Override
    public boolean unscheduleJob(String triggerName) {
        boolean result = false;
        try {
            result = getScheduler().unscheduleJob(TriggerKey.triggerKey(triggerName, DEFAULT_GROUP));
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Unschedule the job, triggerName={}, groupName={}, result={}", triggerName, DEFAULT_GROUP, result);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to unschedule the job, triggerName={}, groupName={}", 
                triggerName, DEFAULT_GROUP, e);
            throw RuntimeErrorException.errorWith(ScheduleServiceErrorCode.SCOPE, 
                ScheduleServiceErrorCode.ERROR_UNSCHEDULE_JOB_FAILED, "Failed to unschedule the job");
        }
        
        return result;
    }
    
    @Override
    public List<ScheduleJobInfoDTO> listScheduleJobs() {
        List<ScheduleJobInfoDTO> jobInfoList = new ArrayList<ScheduleJobInfoDTO>();
        try {
            List<String> groupNameList = getScheduler().getJobGroupNames();
            for(String groupName : groupNameList) {
                Set<JobKey> jobSet = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                for(JobKey jobKey : jobSet) {
                    @SuppressWarnings("rawtypes")
                    List triggers = getScheduler().getTriggersOfJob(jobKey);
                    for(Object trigger : triggers) {
                        ScheduleJobInfoDTO jobInfo = new ScheduleJobInfoDTO();
                        jobInfoList.add(jobInfo);
                        
                        jobInfo.setJobGroupName(jobKey.getGroup());
                        jobInfo.setJobName(jobKey.getName());
                        
                        // 默认信息
                        Trigger defaultTrigger = (Trigger)trigger;
                        jobInfo.setTriggerType("Trigger");
                        jobInfo.setTriggerGroupName(defaultTrigger.getKey().getGroup());
                        jobInfo.setTriggerName(defaultTrigger.getKey().getName());
                        jobInfo.setTriggerState(convertTriggerState(getScheduler().getTriggerState(defaultTrigger.getKey())));
                        if(defaultTrigger.getStartTime() != null) {
                            jobInfo.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defaultTrigger.getStartTime()));
                        }
                        if(defaultTrigger.getEndTime() != null) {
                            jobInfo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defaultTrigger.getEndTime()));
                        }
                        if(defaultTrigger.getPreviousFireTime() != null) {
                            jobInfo.setPreviousFireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defaultTrigger.getPreviousFireTime()));
                        }
                        if(defaultTrigger.getNextFireTime() != null) {
                            jobInfo.setNextFireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defaultTrigger.getNextFireTime()));
                        }
                        if(defaultTrigger.getFinalFireTime() != null) {
                            jobInfo.setFinalFireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(defaultTrigger.getFinalFireTime()));
                        }
                        
                        // 各种不同trigger独有信息
                        if(trigger instanceof CalendarIntervalTrigger) {
                            CalendarIntervalTrigger calendarTrigger = (CalendarIntervalTrigger)trigger;
                            jobInfo.setTriggerType("CalendarIntervalTrigger");
                            jobInfo.setRepeatInterval((long)calendarTrigger.getRepeatInterval());
                            break;
                        }
                        
                        if(trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger)trigger;
                            jobInfo.setTriggerType("CronTrigger");
                            jobInfo.setCronExpression(cronTrigger.getCronExpression());
                            break;
                        }

                        if(trigger instanceof SimpleTrigger) {
                            SimpleTrigger simpleTrigger = (SimpleTrigger)trigger;
                            jobInfo.setTriggerType("SimpleTrigger");
                            jobInfo.setRepeatInterval(simpleTrigger.getRepeatInterval());
                            jobInfo.setRepeatCount(simpleTrigger.getRepeatCount());
                            break;
                        } 
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to list job/trigger info", e);
        }
        
        return jobInfoList;
    }
    
    @SuppressWarnings("rawtypes")
    private Class createJobClass(String jobClassName) {
        try {
            return Class.forName(jobClassName);
        } catch(Exception e) {
            LOGGER.error("Invalid job class name, jobClassName={}", jobClassName, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid job class name");
        }
    }
    
    private String convertTriggerState(TriggerState state) {
        String stateStr = null;
        if(state == null) {
            return stateStr;
        }
        
        switch(state) {
        case BLOCKED:
            stateStr = "BLOCKED";
            break;
        case COMPLETE:
            stateStr = "COMPLETE";
            break;
        case ERROR:
            stateStr = "ERROR";
            break;
        case NONE:
            stateStr = "NONE";
            break;
        case NORMAL:
            stateStr = "NORMAL";
            break;
        case PAUSED:
            stateStr = "PAUSED";
            break;
        }
        
        return stateStr;
    }

    @Override
    public Byte getRunningFlag() {
        return localRunningFlag;
    }

    @Override
    public void setRunningFlag(Byte runningFlag) {
        this.runningFlag = runningFlag;
        setLocalRuningFlag();
    }

    private void setLocalRuningFlag(){
        latestTime = System.currentTimeMillis();
        String delayTimeStr = configurationProvider.getValue("schedule.running.delay.time", "10000");
        Long delayTime = Long.valueOf(delayTimeStr);
        LOGGER.debug("set runningFlag. runningFlag = {}", runningFlag);
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(delayTime);
                }catch (Exception e){
                    LOGGER.error("thread sleep error", e);
                }
                if(System.currentTimeMillis() - latestTime >= delayTime){
                    LOGGER.debug("reset local runningFlag. runningFlag = {}, localRunningFlag = {}", runningFlag, localRunningFlag);
                    if(RunningFlag.fromCode(runningFlag) != RunningFlag.fromCode(localRunningFlag)){
                        localRunningFlag = runningFlag;
                    }
                }else{
                    LOGGER.debug("Hosts are frequently switched.Not set local runningFlag..");
                }
            }
        });

    }
}
 