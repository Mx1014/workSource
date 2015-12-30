package com.everhomes.rest.app;

import javax.validation.constraints.NotNull;

public class VerifyAppUrlBindingCommand {
    
    @NotNull
    private String appKey;
    
    @NotNull
    private String url;

    @NotNull
    private Long timestamp;

    @NotNull
    private Long nonce;

    @NotNull
    private String signature;
    
    public VerifyAppUrlBindingCommand() {
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
