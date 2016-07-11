package com.everhomes.aclink.lingling;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface AclinkLinglingService {

    ListenableFuture<ResponseEntity<String>> restCall(String cmd, Object params,
            ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException;

    String restCall(String cmd, Object params);

    Map<Long, String> makeSdkKey(AclinkLinglingMakeSdkKey sdkKey);

    String getLinglingId();

    AclinkLinglingQRCode getQrCode(AclinkLinglingQrCodeRequest request);

    Long createDevice(AclinkLinglingDevice device);

    void updateDevice(AclinkLinglingDevice device);

    void deleteDevice(AclinkLinglingDevice device);
    
}