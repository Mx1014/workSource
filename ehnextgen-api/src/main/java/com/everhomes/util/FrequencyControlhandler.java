package com.everhomes.util;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class FrequencyControlhandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyControl.class);

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

//    @Before("execution(public String com.captaindebug.audit.controller.*Controller.*(..)) && @annotation(auditAnnotation)")
    public void requestControl(final JoinPoint joinPoint, FrequencyControl control) throws FrequencyControlException {
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (request == null) {
                throw new FrequencyControlException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(ip);

            Map<String, Integer> coutMap = new HashMap<String, Integer>();

            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            Object redisCount = redisTemplate.opsForValue().get(key);
            if (redisCount == null) {
                redisTemplate.opsForValue().set(key, 1, control.time(), TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().increment(key, 1);
                if (Integer.valueOf(redisTemplate.opsForValue().get(key).toString()) > control.count()) {
                    LOGGER.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + control.count() + "]");
                    throw new FrequencyControlException("\"用户IP[\" + ip + \"]访问地址[\" + url + \"]超过了限定的次数[\" + control.count() + \"]\"");
                }
            }

        } catch (FrequencyControlException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("频率控制发生异常: ", e);
        }
    }
}
