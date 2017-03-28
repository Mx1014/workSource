package com.everhomes.messaging;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

@Component
public class MessagingKickoffServiceImpl implements MessagingKickoffService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingKickoffServiceImpl.class);
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;

    @Autowired
    private UserService userService;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private String messageBoxPrefix = "kickoff:";
    
    @Override
    public String getKickoffMessageKey(Integer namespaceId, LoginToken loginToken) {
        long impId = (loginToken.getImpId() == null? 0: loginToken.getImpId().longValue());
        String id = String.format("%d:%d:%d:%d", loginToken.getUserId(), loginToken.getUserId(), loginToken.getLoginId(), impId);
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
    public void removeKickoffTag(Integer namespaceId, LoginToken loginToken) {
        String key = getKickoffMessageKey(namespaceId, loginToken);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.delete(key);
        Object o = redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void checkKickoffStatus(HttpServletRequest request) {
        LoginToken loginToken = userService.getLoginToken(request);
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if(namespaceId != null && loginToken != null && isKickoff(namespaceId, loginToken)) {
            removeKickoffTag(namespaceId, loginToken);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
        }
    }
}
