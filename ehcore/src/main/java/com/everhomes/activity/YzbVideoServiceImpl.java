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
    
    public String startLive(String deviceId) {
        // {"retval": "EOK", "retinfo":{"dsthostname":"118.26.134.12"
        // , "dsthostport":1935, "dstprotocol":"rtmp"
        // , "dstexkey":"rtmp://118.26.134.12:1935/live/AKAauzzOeZu636", "video_enable":1, "audio_enable":1}}
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(YzbConstant.START_LIVE);
        String body = String.format("%s?devid=%s&version=1.0.1&devip=127.0.0.1", url, deviceId);
        
        URI uri = URI.create(body);
        ListenableFuture<ResponseEntity<String>> future = template.getForEntity(uri, String.class);
        ResponseEntity<String> resp;
        try {
            resp = future.get();
            if(resp.getStatusCode() != HttpStatus.OK) {
                return null;
            }
            String result = resp.getBody();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("yzb get error deviceId=" + deviceId, e);
            return null;
        }
    }
}
