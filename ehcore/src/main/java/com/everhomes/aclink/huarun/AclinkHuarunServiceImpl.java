package com.everhomes.aclink.huarun;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;

@Component
public class AclinkHuarunServiceImpl implements AclinkHuarunService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkHuarunServiceImpl.class);
	
    @Autowired
    private ConfigurationProvider  configProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    private Gson gson = new Gson();
    private Random randomGenerator = new Random();
    
    private String getRestUri(String methodName) {
        String serverUrl = configProvider.getValue(AclinkHuarunConstant.HUARUN_SERVER, "https://120.237.113.218:53333");
        
        StringBuffer sb = new StringBuffer(serverUrl);
        if(!serverUrl.endsWith("/"))
            sb.append("/");
        
        sb.append(methodName);
        
        return sb.toString();
    }
    
    @Override
    public ResponseEntity<String> restCall(String cmd, Object params) {
        RestTemplate template = new RestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        String body = StringHelper.toJsonString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        SimpleClientHttpRequestFactory asyncRequestFactory = new HuarunSimpleClientHttpRequestFactory();
        template.setRequestFactory(asyncRequestFactory);
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<String> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return future;
    }
    
    //{"status":0,"user":[{"name":"test","phone":"13800138000","sex":"男","officetel":"","certtype":0,"certno":""}]}
    //{'phone': '13800138000', 'type': '0', 'auth': '1235', 'md5': '864993168D07511229F8DBD471DCBE57'}
    //{"status":0,"qrcode":"zPW~"}
    @Override
    public AclinkHuarunVerifyUserResp verifyUser(AclinkHuarunVerifyUser user) {
        try {
            ResponseEntity<String> future = this.restCall("/crland/verifyUser", user);
            String body = future.getBody();
            if(LOGGER.isDebugEnabled()) {
            	LOGGER.debug("huarun verifyUser" + body);	
            }
            
            AclinkHuarunVerifyUserResp resp = (AclinkHuarunVerifyUserResp)StringHelper.fromJsonString(body, AclinkHuarunVerifyUserResp.class);
            return resp;
        } catch (Exception e) {
            LOGGER.error("huarun request error", e);
            return null;
        }
    }
    
    @Override
    public AclinkGetSimpleQRCodeResp getSimpleQRCode(AclinkGetSimpleQRCode getCode) {
        try {
        		String phone = "13811138111";
        		int n = randomGenerator.nextInt(9999);
        		if(n < 1000) {
        			n = 9944;
        		}
	        	getCode.setAuth(String.valueOf(n));
	        	if(getCode.getPhone() == null) {
	        		getCode.setPhone(phone);	
	        	}
	        	if(getCode.getInvitation() == null) {
		        	getCode.setType("0");
	        	} else {
		        	getCode.setType("1");
	        	}
	        	
	        	String key = String.format("dooraccess:%%s:huarun", phone);
            Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
            RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
            Object v = redisTemplate.opsForValue().get(key);
            if(v != null) {
            	AclinkGetSimpleQRCodeResp redisResp = new AclinkGetSimpleQRCodeResp();
            	redisResp.setQrcode(v.toString());
            	redisResp.setStatus("0");
            	return redisResp;
            }
	        	
	        	MessageDigest md = MessageDigest.getInstance("MD5");
	        	String md5 = "SA" + getCode.getAuth(); 
	        	md.update(md5.getBytes());
	        	String rlt = StringHelper.toHexString(md.digest()).toUpperCase();
	        	getCode.setMd5(rlt);
        	
            ResponseEntity<String> future = this.restCall("/crland/getSimpleQRCode", getCode);
            String body = future.getBody();
            if(LOGGER.isDebugEnabled()) {
            	LOGGER.debug("huarun verifyUser" + body);	
            }
            
            AclinkGetSimpleQRCodeResp resp = (AclinkGetSimpleQRCodeResp)StringHelper.fromJsonString(body, AclinkGetSimpleQRCodeResp.class);
            redisTemplate.opsForValue().set(key, resp.getQrcode(), 2, TimeUnit.HOURS);
            return resp;
        } catch (Exception e) {
            LOGGER.error("huarun request error", e);
            return null;
        }
    }
    
    @Override
    public AclinkHuarunSyncUserResp syncUser(AclinkHuarunSyncUser syncUser) {
        try {
    		int n = randomGenerator.nextInt(9999);
    		if(n < 1000) {
    			n = 9944;
    		}
    		syncUser.setAuth(String.valueOf(n));
        	
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	String md5 = "SA" + syncUser.getAuth(); 
        	md.update(md5.getBytes());
        	String rlt = StringHelper.toHexString(md.digest()).toUpperCase();
        	syncUser.setMd5(rlt);
    	
        ResponseEntity<String> future = this.restCall("/crland/syncUser", syncUser);
        String body = future.getBody();
        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("huarun verifyUser" + body);	
        }
        
        AclinkHuarunSyncUserResp resp = (AclinkHuarunSyncUserResp)StringHelper.fromJsonString(body, AclinkHuarunSyncUserResp.class);
        return resp;
    } catch (Exception e) {
        LOGGER.error("huarun request error", e);
        return null;
    }
    }
}
