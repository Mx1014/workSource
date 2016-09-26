package com.everhomes.activity;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.util.StringHelper;

@Component
public class YzbVideoServiceImpl implements YzbVideoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(YzbVideoServiceImpl.class);
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    private String getRestUri(String methodName) {
        String serverUrl = configProvider.getValue(YzbConstant.YZB_SERVER, YzbConstant.YZB_SERVER_DEFAULT);
        
        StringBuffer sb = new StringBuffer(serverUrl);
        if(!serverUrl.endsWith("/"))
            sb.append("/");
        
        sb.append(methodName);
        
        return sb.toString();
    }
    
    @Override
    public YzbLiveVideoResponse startLive(String deviceId) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(YzbConstant.START_LIVE);
        String body = String.format("%s?devid=%s&version=1.0.1&devip=127.0.0.1", url, deviceId);
        
        URI uri = URI.create(body);
        ListenableFuture<ResponseEntity<String>> future = template.getForEntity(uri, String.class);
        try {
            ResponseEntity<String> resp = future.get();
            if(resp.getStatusCode() != HttpStatus.OK) {
                return null;
            }
            YzbLiveVideoResponse result = (YzbLiveVideoResponse)StringHelper.fromJsonString(resp.getBody(), YzbLiveVideoResponse.class);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("yzb get error deviceId=" + deviceId, e);
            return null;
        }
    }
    
    @Override
    public YzbLiveVideoResponse setContinue(String deviceId, int b) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(YzbConstant.SET_CONTINUE);
        String body = String.format("%s??devid=%s&continue=%d", url, deviceId, b);
        
        URI uri = URI.create(body);
        ListenableFuture<ResponseEntity<String>> future = template.getForEntity(uri, String.class);
        try {
            ResponseEntity<String> resp = future.get();
            if(resp.getStatusCode() != HttpStatus.OK) {
                return null;
            }
            YzbLiveVideoResponse result = (YzbLiveVideoResponse)StringHelper.fromJsonString(resp.getBody(), YzbLiveVideoResponse.class);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("yzb setcontinue error deviceId=" + deviceId, e);
            return null;
        }
    }
}
