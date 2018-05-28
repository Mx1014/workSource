package com.everhomes.pay.oauth2;

public interface AccessTokenManager {
    String getAccessToken(String sessionId);

    void setAccessToken(String sessionId, String token);
}
