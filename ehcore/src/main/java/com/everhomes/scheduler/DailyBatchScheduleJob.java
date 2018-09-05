package com.everhomes.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.remind.RemindService;

/**
 * 日定时处理任务
 */
@Component
public abstract class DailyBatchScheduleJob extends QuartzJobBean implements ApplicationListener<ContextRefreshedEvent> { 
	
    @Autowired
    protected RemindService remindService;
    @Autowired
    protected ScheduleProvider scheduleProvider;
    @Autowired
    protected BigCollectionProvider bigCollectionProvider;
    @Autowired
    protected CoordinationProvider coordinationProvider;
 
    private ValueOperations<String, String> getValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations;
    }
    /**
     * 业务需要实现装载数据的接口(zset和value两个redis)
     * */
    protected abstract void load();
    
    private void setVO(String key , String i, int timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = getValueOperations(key);
       
        valueOperations.set(key, i, timeout, unit);
    }
 
	private ZSetOperations<String, Long> getZSetOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ZSetOperations<String, Long> valueOperations = redisTemplate.opsForZSet();
        return valueOperations;
    }

    private void setZSet(Long targetId, Long timestampLong, String ZSET_KEY){
    	ZSetOperations<String, Long> zSetOperations = getZSetOperations(ZSET_KEY); 
    	zSetOperations.add(ZSET_KEY, targetId, long2Double(timestampLong));
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
    public Set<String> range(Long beginTime,Long endTime,String ZSET_KEY, String VOKeyPrefix){
    	Set<String> objects = new HashSet<>();
    	ZSetOperations<String, Long> zSetOperations = getZSetOperations(ZSET_KEY); 
    	Set<Long> ids = zSetOperations.rangeByScore(ZSET_KEY, beginTime.doubleValue(), endTime.doubleValue());
    	ids.forEach(id ->{
    		String key = VOKeyPrefix + id;
    		ValueOperations<String, String> vo = getValueOperations(key);
    		String o = vo.get(key);
    		objects.add(o);
    	});
    	return objects;
    }
    /**
     * 添加/修改数据
     * @param targetId : 目标对象的唯一id
     * @param timestampLong : 提醒时间 用以zset排序和到期提醒
     * @param VOKeyPrefix : valueOperation的key前缀每个业务都必须不一样(前缀+id 作为key)
     * @param i : 缓存的业务对象的jsonString
     * @param ZSET_KEY : zSetOperations的key 每个业务都不一样
     * @param timeout : 数据超时时间
     * @param unit : 超时时间单位
     * */
    public void set(Long targetId, Long timestampLong, String VOKeyPrefix , String i, String ZSET_KEY, int timeout, TimeUnit unit){
    	ZSetOperations<String, Long> zSetOperations = getZSetOperations(ZSET_KEY); 
    	zSetOperations.remove(ZSET_KEY, targetId);
    	setZSet(targetId, timestampLong, ZSET_KEY);
    	setVO(VOKeyPrefix + targetId, i, timeout, unit);
    }

    /**
     * 取消删除数据
     * @param targetId : 目标对象的唯一id
     * @param VOKeyPrefix : valueOperation的key前缀每个业务都必须不一样(前缀+id 作为key)
     * @param i : 缓存的业务对象
     * @param ZSET_KEY : zSetOperations的key 每个业务都不一样
     * @param timeout : 数据超时时间
     * @param unit : 超时时间单位
     * */
    public void cancel(Long targetId, String VOKeyPrefix , Object i, String ZSET_KEY, int timeout, TimeUnit unit){
    	ZSetOperations<String, Long> zSetOperations = getZSetOperations(ZSET_KEY); 
    	zSetOperations.remove(ZSET_KEY, targetId);
    	setVO(VOKeyPrefix + targetId, null, timeout, unit);
    }
}
