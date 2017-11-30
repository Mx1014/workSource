package com.everhomes.aclink.lingling;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;

@Component
public class AclinkLinglingServiceImpl implements AclinkLinglingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkLinglingServiceImpl.class);
   
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    Gson gson = new Gson();
    
    private String getRestUri(String methodName) {
        String serverUrl = configProvider.getValue(AclinkLinglingConstant.LINGLING_SERVER, "");
        String appKey = configProvider.getValue(AclinkLinglingConstant.LINGLING_APPKEY, "");
        
        StringBuffer sb = new StringBuffer(serverUrl);
        if(!serverUrl.endsWith("/"))
            sb.append("/");
        
        sb.append(methodName);
        sb.append("/");
        sb.append(appKey);
        
        return sb.toString();
    }
    
    @Override
    public ListenableFuture<ResponseEntity<String>> restCall(String cmd, Object params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException {
        String signature = configProvider.getValue(AclinkLinglingConstant.LINGLING_SIGNATURE, "");
        String token = configProvider.getValue(AclinkLinglingConstant.LINGLING_TOKEN, "");
        
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        
        AclinkLinglingRequest linglingReq = new AclinkLinglingRequest();
        
        if(params != null) {
            linglingReq.setRequestParam(params);
        }
        
        Map<String, String> authHeader = new HashMap<String, String>();
        authHeader.put("signature", signature);
        authHeader.put("token", token);
        linglingReq.setHeader(authHeader);
        
        String body = "MESSAGE=" + URLEncoder.encode(gson.toJson(linglingReq, AclinkLinglingRequest.class));
        //String body = "MESSAGE=" + URLEncoder.encode("{requestParam:{deviceIds:[1008], keyEffecDay:200}, header:{signature:'f2877f02-5638-45ab-8425-8bd198f36a9b', token:1461381932233}}");
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        
        if(responseCallback != null) {
            future.addCallback(responseCallback);    
        }
        
        return future;
    }
    
    @Override
    public String restCall(String cmd, Object params) {
        try {
            ListenableFuture<ResponseEntity<String>> future = this.restCall(cmd, params, null);
            return future.get().getBody();
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
            LOGGER.error("lingling request error", e);
            return "";
        }
        
    }
    
    private String getKeyFromSdkKey(AclinkLinglingMakeSdkKey sdkKey) {
        Object[] objs = sdkKey.getDeviceIds().toArray();
        Arrays.sort(objs);
        
        String key = "";
        for(Object obj: objs) {
            key += obj.toString() + ":";
        }
        
        return key;
    }
    
    @Override
    public Map<Long, String> makeSdkKey(AclinkLinglingMakeSdkKey sdkKey) {
        if(sdkKey.getDeviceIds().size() <= 0) {
            return null;
        }
        
        String key = getKeyFromSdkKey(sdkKey);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(key);
        
        String body = "";
        AclinkLinglingMapResponse resp = null;
        Map<Long, String> keys = null;
        
        if(o != null) {
            body = (String)o;
            resp = (AclinkLinglingMapResponse)StringHelper.fromJsonString(body, AclinkLinglingMapResponse.class);
            keys = new HashMap<Long, String>();
            for(Entry<String, String> item : resp.getResponseResult().entrySet()) {
                keys.put(Long.parseLong(item.getKey()), item.getValue());
            }
            
            //check cache
            int cache = 0;
            for(Long deviceId : sdkKey.getDeviceIds()) {
                if (keys.get(deviceId) != null) {
                    cache++;
                }
            }
            
            if(cache != sdkKey.getDeviceIds().size()) {
                body = "";
                resp = null;
                keys = null;
                LOGGER.warn("lingling cache failed, sdkKey=" + sdkKey);
            }
            
        }
        
        if(resp == null) {
            body = this.restCall(AclinkLinglingConstant.MAKE_SDK_KEY, sdkKey);
            if(body == "") {
                return null;
            }
            
            //manual cache it to redis
            redisTemplate.opsForValue().set(key, body, 10*60, TimeUnit.SECONDS);
            
            resp = (AclinkLinglingMapResponse)StringHelper.fromJsonString(body, AclinkLinglingMapResponse.class);
            keys = new HashMap<Long, String>();
            for(Entry<String, String> item : resp.getResponseResult().entrySet()) {
                keys.put(Long.parseLong(item.getKey()), item.getValue());
            }
            
        }
        
        return keys;
    }
   
    @Override
    public String getLinglingId() {
        String body = this.restCall(AclinkLinglingConstant.GET_LINGLING_ID, null);
        if(body == "") {
            return null;
        }
        
        AclinkLinglingMapResponse resp = (AclinkLinglingMapResponse)StringHelper.fromJsonString(body, AclinkLinglingMapResponse.class);
        return resp.getResponseResult().get("lingLingId");
    }
    
    @Override
    public AclinkLinglingQRCode getQrCode(AclinkLinglingQrCodeRequest request) {
        String body = this.restCall(AclinkLinglingConstant.ADD_LIFT_QR_CODE, request);
        if(body == "") {
            return null;
        }
        
        AclinkLinglingQRCodeKeyResponse resp = (AclinkLinglingQRCodeKeyResponse)StringHelper.fromJsonString(body, AclinkLinglingQRCodeKeyResponse.class);
        return resp.getResponseResult();        
    }
    
    @Override
    public Long createDevice(AclinkLinglingDevice device) {
        String body = this.restCall(AclinkLinglingConstant.ADD_DEVICE, device);
        if(body == "") {
            return null;
        }
        
        HashMap resp = (HashMap)StringHelper.fromJsonString(body, HashMap.class);
        Object o = resp.get("responseResult");
        if(o.toString().indexOf("{") == -1) {
            LOGGER.error("create device=" + device + ", result=" + o.toString());
            return null;
        }
        
        //{deviceId=1012.0}
        Map result = (Map)o;
        Double devId = (Double)result.get("deviceId");
        
        return devId.longValue();
    }
    
    @Override
    public void updateDevice(AclinkLinglingDevice device) {
        this.restCall(AclinkLinglingConstant.UPDATE_DEVICE, device);
    }
    
    @Override
    public void deleteDevice(AclinkLinglingDevice device) {
        this.restCall(AclinkLinglingConstant.DEL_DEVICE, device);
    }
}