package com.everhomes.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface HttpRestCallProvider {

    void restCall(String cmd, Map<String, String> params,
            ListenableFutureCallback<ResponseEntity<String>> responseCallback);

    ResponseEntity<String> syncRestCall(String cmd, Map<String, String> params) throws InterruptedException, ExecutionException, TimeoutException, UnsupportedEncodingException;
}
