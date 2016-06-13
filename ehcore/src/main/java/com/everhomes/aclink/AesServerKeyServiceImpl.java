package com.everhomes.aclink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;

@Component
public class AesServerKeyServiceImpl implements AesServerKeyService {
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    @Autowired
    AesServerKeyProvider aesServerKeyProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    final String ACKING_SECRET = "dooraccess:%d:acking";
    final String EXPECT_SECRET = "dooraccess:%d:expect";
    
    @Override
    public Long createAesServerKey(AesServerKey obj) {
        obj.setSecretVer(1l);
        Long id = aesServerKeyProvider.createAesServerKey(obj);
        String key = String.format(ACKING_SECRET, obj.getDoorId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.opsForValue().set(key, "1");
        
        key = String.format(EXPECT_SECRET, obj.getDoorId());
        redisTemplate.opsForValue().set(key, "1");
        
        return id;
    }
    
    @Override
    public Integer getAckingSecretVersion(DoorAccess doorAccess) {
        Long doorAccId = doorAccess.getId();
        String key = String.format(ACKING_SECRET, doorAccId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        if(v == null) {
            //Initial
            redisTemplate.opsForValue().setIfAbsent(key, doorAccess.getAckingSecretVersion().toString());
            return 0;
        }
        
        return Integer.valueOf((String)v);
    }
    
    @Override
    public Integer getExpectSecretKey(DoorAccess doorAccess) {
        Long doorAccId = doorAccess.getId();
        String key = String.format(EXPECT_SECRET, doorAccId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        if(v == null) {
            //Initial
            redisTemplate.opsForValue().setIfAbsent(key, doorAccess.getExpectSecretKey().toString());
            return 0;
        }
        
        return Integer.valueOf((String)v);
    }
    
    private String getRedisValue(String key) {
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        if(v == null) {
            return null;
        }
        
        return (String)v;
    }
    
    @Override
    public AesServerKey getCurrentAesServerKey(Long doorAccId) {
        String key = String.format(ACKING_SECRET, doorAccId);
        String ver = getRedisValue(key);
        if(ver == null) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(doorAccId);
            if(doorAccess == null) {
                return null;
            }
            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            
            key = String.format(EXPECT_SECRET, doorAccId);
            redisTemplate.opsForValue().set(key, doorAccess.getExpectSecretKey().toString());
            
            key = String.format(ACKING_SECRET, doorAccId);
            redisTemplate.opsForValue().set(key, doorAccess.getAckingSecretVersion().toString());
            
            ver = doorAccess.getAckingSecretVersion().toString();
        }
        
        Long verId = Long.valueOf(ver);
        return aesServerKeyProvider.queryAesServerKeyByDoorId(doorAccId, verId);
    }
}
