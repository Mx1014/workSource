package com.everhomes.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AccessTokenManager{

    private Map<String, String> tokenMap = new HashMap<>();

    public String getAccessToken(String sessionId) {
        synchronized (tokenMap) {
            return tokenMap.get(sessionId);
        }
    }

    public void setAccessToken(String sessionId, String token) {
        synchronized (tokenMap) {
            tokenMap.put(sessionId, token);
        }
    }
}
