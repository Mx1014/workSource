package com.everhomes.scheduler;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
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
public class CalendarRemindScheduleJob extends  QuartzJobBean implements ApplicationListener<ContextRefreshedEvent> {
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
    protected ScheduleProvider scheduleProvider;
    @Autowired
    protected BigCollectionProvider bigCollectionProvider;
    @Autowired
    protected CoordinationProvider coordinationProvider;
    protected static int TIMEOUT = 25;
    protected static TimeUnit UNIT = TimeUnit.HOURS;
    /**
     * 每天早上0点2分加载今天的remind到redis
     * */
    @Scheduled(cron = "0 2 0 * * ? ")
	public void load() {
    	 if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
         	coordinationProvider.getNamedLock(CoordinationLocks.REMIND_SCHEDULED.getCode() + "load").tryEnter(() -> {
         		//取今天0点到第二天0点的数据
                 Timestamp remindEndTime = new Timestamp(DateStatisticHelper.getCurrent0Hour().getTime() + 24*3600*1000L);
                 Timestamp remindStartTime = new Timestamp(DateStatisticHelper.getCurrent0Hour().getTime()); 
                 int offset = 1;
                 List<Remind> reminds = remindProvider.findUndoRemindsByRemindTimeByPage(remindStartTime, remindEndTime, FETCH_SIZE, offset++);
                 boolean isProcess = !CollectionUtils.isEmpty(reminds);
                 while (isProcess) {
                     reminds.forEach(remind -> {
                         try {
                             set(remind.getId(), remind.getRemindTime().getTime(), remind);
                         } catch (Exception e) {
                             // 忽略单个错误，以免影响到所有提醒消息的推送
                             LOGGER.error("remindSchedule error,remindId = {}", remind.getId(), e);
                         }
                     });
                     if(CollectionUtils.isEmpty(reminds) || reminds.size() < FETCH_SIZE){
                    	 isProcess = false;
                     }else{
                    	 reminds = remindProvider.findUndoRemindsByRemindTimeByPage(remindStartTime, remindEndTime, FETCH_SIZE, offset++);
                     }
                     
                 }
                 LOGGER.debug("load data complete : size{}, begintime {} end time{}",reminds==null?0:reminds.size(),remindStartTime ,remindEndTime);
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
            scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, CalendarRemindScheduleJob.class, null);
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
    	String key = MODULE + targetId;
    	//1毫秒后过期删除
        ValueOperations<String, String> valueOperations = getRedisTemplate(key).opsForValue(); 
        valueOperations.set(key, "", 1, TimeUnit.MICROSECONDS);
    }

	public void set(Long targetId, long timestampLong, Remind remind) {
		String remindJsonString = StringHelper.toJsonString(remind);
		String voKey = MODULE + targetId;
    	ZSetOperations<String, String> zSetOperations = getRedisTemplate(ZSET_KEY).opsForZSet();
    	zSetOperations.add(ZSET_KEY, String.valueOf(targetId), long2Double(timestampLong));
        ValueOperations<String, String> valueOperations = getRedisTemplate(voKey).opsForValue(); 
        valueOperations.set(voKey, remindJsonString, TIMEOUT, UNIT);
	} 
  
	private RedisTemplate<String, String> getRedisTemplate(String key){
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate<String, String> redisTemplate = acc.getTemplate(stringRedisSerializer);
        return redisTemplate;
	} 
    
    private double long2Double(Long timestampLong) {
		if(null != timestampLong){
			return timestampLong.doubleValue();
		}
		return 0d;
	}
    /**
     * 根据时间范围提取数据 
     * @param beginTime : 过滤开始时间
     * @param endTime : 过滤结束时间
     * @param VOKeyPrefix : valueOperation的key前缀每个业务都必须不一样(前缀+id 作为key)
     * @param ZSET_KEY : zSetOperations的key 每个业务都不一样
     * 
     * @return : 返回数据对象的排序Set
     * */
    public Set<String> range(Long beginTime,Long endTime,String key, String VOKeyPrefix){
    	Set<String> objects = new HashSet<>();
    	ZSetOperations<String, String> zSetOperations = getRedisTemplate(key).opsForZSet();
    	Set<String> ids = zSetOperations.rangeByScore(key, beginTime.doubleValue(), endTime.doubleValue());
    	ids.forEach(id ->{
    		String vokey = VOKeyPrefix + id;
    		ValueOperations<String, String> vo =  getRedisTemplate(vokey).opsForValue(); 
    		String o = vo.get(vokey);
    		objects.add(o);
    	});
    	try{
    		//清除已经提取过的元素
        	zSetOperations.removeRangeByScore(key, 0.0, endTime.doubleValue());
    	}catch(Exception e){
    		LOGGER.error("remove zset error :", e);
    	}
    	return objects;
    } 
 
}
