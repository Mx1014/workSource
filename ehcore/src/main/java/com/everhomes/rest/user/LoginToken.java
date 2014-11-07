package com.everhomes.rest.user;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

public class LoginToken {
    private long userId;
    private long deviceId;
    private int deviceLoginInstanceNumber;
    
    public LoginToken() {
    }

    public LoginToken(long userId, long deviceId, int instanceNumber) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceLoginInstanceNumber = instanceNumber;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceLoginInstanceNumber() {
        return deviceLoginInstanceNumber;
    }

    public void setDeviceLoginInstanceNumber(int deviceLoginInstanceNumber) {
        this.deviceLoginInstanceNumber = deviceLoginInstanceNumber;
    }
    
    public String getTokenString() {
        String json = new Gson().toJson(this);
        return Base64.encodeBase64URLSafeString(json.getBytes());
    }
    
    public static LoginToken fromTokenString(String tokenString) {
        String json = new String(Base64.decodeBase64(tokenString));
        return (LoginToken)new Gson().fromJson(json, LoginToken.class);
    }
}
