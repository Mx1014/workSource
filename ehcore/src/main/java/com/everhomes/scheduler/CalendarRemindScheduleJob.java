package com.everhomes.scheduler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.remind.Remind;
import com.everhomes.remind.RemindProvider;
import com.everhomes.remind.RemindService;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateStatisticHelper;
import com.everhomes.util.StringHelper;

/**
 * 日程定时提醒任务
 */
@Component
public class CalendarRemindScheduleJob extends DailyBatchScheduleJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarRemindScheduleJob.class);
    public static final String MODULE = "calendar-remind-";
    public static String CRON_EXPRESSION = "0 0/5 * * * ? ";
    protected static final String ZSET_KEY = "daily-remind-zset";
	private static final int FETCH_SIZE = 1000;
	private static final String CALENDAR_REMIND_SCHEDULE = "remind-scheduled-";
    @Autowired
    private RemindService remindService;
    @Autowired
    private RemindProvider remindProvider;
    @Autowired
    private ScheduleProvider scheduleProvider;
    protected static int timeout = 25;
    protected static TimeUnit unit = TimeUnit.HOURS;
    /**
     * 每天早上0点2分加载今天的remind到redis
     * */
    @Scheduled(cron = "0 2 0 * * ? ")
	@Override
	public void load() {
		// TODO Auto-generated method stub
    	 if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
         	coordinationProvider.getNamedLock(CoordinationLocks.REMIND_SCHEDULED.getCode() + "load").tryEnter(() -> {
         		//取今天0点到第二天0点的数据
                 Timestamp remindEndTime = new Timestamp(DateStatisticHelper.getCurrent0Hour().getTime() + 24*3600*1000L);
                 Timestamp remindStartTime = new Timestamp(DateStatisticHelper.getCurrent0Hour().getTime()); 
                 List<Remind> reminds = remindProvider.findUndoRemindsByRemindTime(remindStartTime, remindEndTime, FETCH_SIZE);
                 long count = 0;
                 boolean isProcess = !CollectionUtils.isEmpty(reminds);
                 while (isProcess) {
                     count += reminds.size();
                     reminds.forEach(remind -> {
                         try {
                             set(remind.getId(), remind.getRemindTime().getTime(), remind);
                         } catch (Exception e) {
                             // 忽略单个错误，以免影响到所有提醒消息的推送
                             LOGGER.error("remindSchedule error,remindId = {}", remind.getId(), e);
                         }
                     });
                     reminds = remindProvider.findUndoRemindsByRemindTime(remindStartTime, remindEndTime, FETCH_SIZE);
                     isProcess = !CollectionUtils.isEmpty(reminds);
                 }
         	});
         }
	}
    
    /**
     * 每次取redis的数据 发消息
     * 然后remove之前的数据 update remind
     * */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 现网是集群部署，这个判断是为了防止定时任务在多台机器执行
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
        	coordinationProvider.getNamedLock(CoordinationLocks.REMIND_SCHEDULED.getCode()).tryEnter(() -> {
        		//改成每次提醒6分钟前到现在的
                Long remindEndTime = DateHelper.currentGMTTime().getTime();
                Long remindStartTime = DateHelper.currentGMTTime().getTime() - 6*60*1000L;
                Set<String> reminds = range(remindStartTime, remindEndTime, ZSET_KEY, MODULE);
                reminds.forEach(jsonStr -> {
                    try {
                    	Remind remind =  (Remind) StringHelper.fromJsonString(jsonStr, Remind.class);
                    	if(remind == null || remind.getRemindTime() == null){
                    		return;
                    	}
                    	remindService.sendRemindMessage(remind);
                        remind.setActRemindTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        remindProvider.updateRemind(remind); 
                        cancel(remind.getId());
                        set(remind.getId(), remind.getRemindTime().getTime(), remind);
                    } catch (Exception e) {
                        // 忽略单个错误，以免影响到所有提醒消息的推送
                        LOGGER.error("remindSchedule error,remind = {}", jsonStr , e);
                    }
                });
           	 	 
        	});
        }
    }

    public void setup() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            String triggerName = CALENDAR_REMIND_SCHEDULE + System.currentTimeMillis();
            String jobName = triggerName;
            String cronExpression = CRON_EXPRESSION;
            //启动定时任务
            scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, DailyBatchScheduleJob.class, null);
        }
    }
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
            //启动的时候加载一次数据
            load();
        }
    }

    public void cancel(Long targetId){
    	cancel(targetId, MODULE  + targetId, null, ZSET_KEY, timeout, unit);
    }

	public void set(Long targetId, long timestampLong, Remind remind) {
		String remindJsonString = StringHelper.toJsonString(remind);
		set(targetId, timestampLong, MODULE  + targetId, remindJsonString, ZSET_KEY, timeout, unit);
	}
}
