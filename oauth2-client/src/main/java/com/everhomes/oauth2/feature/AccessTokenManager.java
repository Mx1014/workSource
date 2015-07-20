package com.everhomes.oauth2.feature;

public interface AccessTokenManager {
    String getAccessToken(String sessionId);
    void setAccessToken(String sessionId, String token);
}
