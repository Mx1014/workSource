package com.everhomes.controller;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
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

import com.everhomes.util.SignatureHelper;

@Component
public class HttpRestCallProviderImpl implements HttpRestCallProvider {
    @Value("${border.id}")
    private int borderId;
    
    @Value("${core.service.uri}")
    private String coreServiceUri;
    
    @Value("${border.app.key}")
    private String appKey;
    
    @Value("${border.app.secret}")
    private String secretKey;
    
    private String getRestUri(String relativeUri) {
        StringBuffer sb = new StringBuffer(this.coreServiceUri);
        if(!this.coreServiceUri.endsWith("/"))
            sb.append("/");
        
        if(relativeUri.startsWith("/"))
            sb.append(relativeUri.substring(1));
        else
            sb.append(relativeUri);
        
        return sb.toString();
    }    
    
    @Override
    public void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        params.put("appKey", this.appKey);
        String signature = SignatureHelper.computeSignature(params, this.secretKey);
        params.put("signature", signature);
        
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for(Map.Entry<String, String> entry: params.entrySet()) {
            paramMap.add(entry.getKey(), entry.getValue());
        }
        
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(paramMap,headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        future.addCallback(responseCallback);
    }
    
    @Override
    public ResponseEntity<String> syncRestCall(String cmd, Map<String, String> params) throws InterruptedException, ExecutionException, TimeoutException {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        params.put("appKey", this.appKey);
        String signature = SignatureHelper.computeSignature(params, this.secretKey);
        params.put("signature", signature);
        
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for(Map.Entry<String, String> entry: params.entrySet()) {
            paramMap.add(entry.getKey(), entry.getValue());
        }
        
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<MultiValueMap<String,String>>(paramMap,headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        ResponseEntity<String> entity = future.get(8, TimeUnit.SECONDS);
        return entity;
    }
}
