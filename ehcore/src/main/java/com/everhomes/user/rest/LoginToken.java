package com.everhomes.user.rest;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

/**
 * 
 * TODO add signature to protect against tempering
 * 
 * Represents token object for client login
 * property loginId is unique only on per-user basis (not a global unique identifier)
 * 
 * @author Kelven Yang
 *
 */
public class LoginToken {
    private long userId;
    private int loginId;
    private int loginInstanceNumber;
    
    public LoginToken() {
    }

    public LoginToken(long userId, int loginId, int instanceNumber) {
        this.userId = userId;
        this.loginId = loginId;
        this.loginInstanceNumber = instanceNumber;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getLoginInstanceNumber() {
        return loginInstanceNumber;
    }

    public void setLoginInstanceNumber(int loginInstanceNumber) {
        this.loginInstanceNumber = loginInstanceNumber;
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
