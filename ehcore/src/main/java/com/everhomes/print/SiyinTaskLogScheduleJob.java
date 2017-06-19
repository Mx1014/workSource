package com.everhomes.print;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.xml.XMLToJSON;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.quartz.QuartzJobBean;
import sun.misc.BASE64Decoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sfyan on 2017/3/23.
 */
public class SiyinTaskLogScheduleJob  extends QuartzJobBean {

    private String siyinUrl = "http://siyin.zuolin.com:8119";

    private static final Logger LOGGER = LoggerFactory.getLogger(SiyinTaskLogScheduleJob.class);

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try{
            final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            String key = "startTime";
            // 需要把查询的开始时间持久化 ，暂存储在redis
            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String startTime = valueOperations.get(key);
            if(null == startTime){
                startTime = "2017-03-22 00:00:00";
            }

            String endTime = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_LINE);

            Map<String, String> params = new HashMap<>();
            params.put("start_time", startTime);
            params.put("end_time", endTime);
            String result = HttpUtils.post(siyinUrl + "/console/queryServlet", params, 30);
            String siyinCode = getSiyinCode(result);
            if(siyinCode.equals("OK")){
                LOGGER.warn("siyin api:/console/queryServlet response:{}", getSiyinData(result));
                return;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            result = new String(decoder.decodeBuffer(result));
            siyinCode = getSiyinCode(result);
            if(!siyinCode.equals("OK")){
                LOGGER.warn("siyin api:/console/queryServlet siyinCode:{}", siyinCode);
            }
            String taskData = getSiyinData(result);
            taskData = XMLToJSON.convertStandardJson(taskData);
            valueOperations.set(key, endTime);
            LOGGER.info("api:/console/queryServlet, response:{}", taskData);

        }catch (Exception e){

        }
    }

    public String getSiyinCode(String result){
        if(result.indexOf(":") > 0){
            return result.substring(0, result.indexOf(":"));
        }
        return "";
    }

    public String getSiyinData(String result){
        return result.substring(result.indexOf(":") + 1);
    }
}
