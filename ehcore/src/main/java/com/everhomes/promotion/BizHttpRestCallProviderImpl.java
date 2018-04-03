package com.everhomes.promotion;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.util.SignatureHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class BizHttpRestCallProviderImpl implements BizHttpRestCallProvider {    
//    @Value("${biz.serverUrl}")
//    private String bizServerUrl;
//    
//    @Value("${biz.appKey}")
//    private String appKey;
//    
//    @Value("${biz.secretKey}")
//    private String secretKey;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    private String getRestUri(String relativeUri) {
        
        String bizServerUrl = configProvider.getValue(OpPromotionConstant.BIZ_SERVER_URL, "");
        StringBuffer sb = new StringBuffer(bizServerUrl);
        if(!bizServerUrl.endsWith("/"))
            sb.append("/");
        
        if(relativeUri.startsWith("/"))
            sb.append(relativeUri.substring(1));
        else
            sb.append(relativeUri);
        
        return sb.toString();
    }
    
    @Override
    public void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String appKey = configProvider.getValue(OpPromotionConstant.BIZ_APPKEY, "");
        String secretKey = configProvider.getValue(OpPromotionConstant.BIZ_SECRET_KEY, "");
        params.put("appKey", appKey);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
        
//        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
//        for(Map.Entry<String, String> entry: params.entrySet()) {
//            paramMap.add(entry.getKey(), entry.getValue());
//        }
//        
//        String jsonStr = StringHelper.toJsonString(params);
        
        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<Map<String,String>>(params, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        future.addCallback(responseCallback);
    }

    @Override
    public ResponseEntity<String> syncRestCall(String cmd, Map<String, String> params) throws InterruptedException, ExecutionException, TimeoutException, UnsupportedEncodingException {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String appKey = configProvider.getValue(OpPromotionConstant.BIZ_APPKEY, "");
        String secretKey = configProvider.getValue(OpPromotionConstant.BIZ_SECRET_KEY, "");
        params.put("appKey", appKey);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
        
//        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
//        for(Map.Entry<String, String> entry: params.entrySet()) {
//            paramMap.add(entry.getKey(), entry.getValue());
//        }
        
        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<Map<String,String>>(params, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        ResponseEntity<String> entity = future.get(8, TimeUnit.SECONDS);
        return entity;
    }

    @Override
    public ResponseEntity<String> syncRestCall(String cmd, String jsonBody) throws InterruptedException, ExecutionException, TimeoutException, UnsupportedEncodingException {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String appKey = configProvider.getValue(OpPromotionConstant.BIZ_APPKEY, "");
        String secretKey = configProvider.getValue(OpPromotionConstant.BIZ_SECRET_KEY, "");

        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("appKey", appKey);
        String signature = SignatureHelper.computeSignature(uriParams, secretKey);
        uriParams.put("signature", URLEncoder.encode(signature,"UTF-8"));

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class, uriParams);
        return future.get(8, TimeUnit.SECONDS);
    }
}
