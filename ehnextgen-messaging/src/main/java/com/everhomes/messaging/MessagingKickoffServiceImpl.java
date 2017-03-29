package com.everhomes.messaging;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.rest.user.LoginToken;

@Component
public class MessagingKickoffServiceImpl implements MessagingKickoffService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingKickoffServiceImpl.class);
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private String messageBoxPrefix = "kickoff:";
    
    @Override
    public String getKickoffMessageKey(Integer namespaceId, LoginToken loginToken) {
        long impId = (loginToken.getImpId() == null? 0: loginToken.getImpId().longValue());
        String id = String.format("%d:%d:%d:%d", loginToken.getUserId(), loginToken.getLoginInstanceNumber(), loginToken.getLoginId(), impId);
        if(namespaceId == null || namespaceId.equals(0)) {
            return messageBoxPrefix + ":" + id; 
        } else {
            return messageBoxPrefix + ":" + namespaceId + ":" + id;
        }
    }
    
    @Override
    public void kickoff(Integer namespaceId, LoginToken loginToken) {
        String key = getKickoffMessageKey(namespaceId, loginToken);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 7, TimeUnit.DAYS);
    }
    
    @Override
    public boolean isKickoff(Integer namespaceId, LoginToken loginToken) {
        try {
            String key = getKickoffMessageKey(namespaceId, loginToken);
            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            Object o = redisTemplate.opsForValue().get(key);
            if(o == null) {
                return false;
            }
            
            return true;            
        } catch(Exception ex) {
            LOGGER.info("kickoff error, loginToken error? " + ex.getMessage());   
        }

        return false;
    }
    
    @Override
    public void remoteKickoffTag(Integer namespaceId, LoginToken loginToken) {
        try {
            String key = getKickoffMessageKey(namespaceId, loginToken);
            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            redisTemplate.delete(key);
            Object o = redisTemplate.opsForValue().get(key);
            LOGGER.debug("object=" + o);
          
        } catch(Exception ex) {
            LOGGER.info("kickoff error, loginToken error? " + ex.getMessage());   
        }    	
    }
}
