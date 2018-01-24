package com.everhomes.promotion;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface BizHttpRestCallProvider {

    void restCall(String cmd, Map<String, String> params,
            ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException;

    ResponseEntity<String> syncRestCall(String cmd, Map<String, String> params) throws InterruptedException,
            ExecutionException, TimeoutException, UnsupportedEncodingException;

    ResponseEntity<String> syncRestCall(String cmd, String jsonBody)  throws InterruptedException,
            ExecutionException, TimeoutException, UnsupportedEncodingException;
}
