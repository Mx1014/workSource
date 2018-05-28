package com.everhomes.pay.oauth2;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccessTokenManagerImpl implements AccessTokenManager {

    private Map<String, String> tokenMap = new HashMap<>();

    @Override
    public String getAccessToken(String sessionId) {
        synchronized (tokenMap) {
            return tokenMap.get(sessionId);
        }
    }

    @Override
    public void setAccessToken(String sessionId, String token) {
        synchronized (tokenMap) {
            tokenMap.put(sessionId, token);
        }
    }
}
