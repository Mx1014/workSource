package com.everhomes.client;

import java.util.Map;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.ConsumerCallback;

/**
 * 
 * Defines client API to work with REST and messaging services
 * 
 * @author Kelven Yang
 *
 */
public interface Client {
    //
    // Configuration
    //
    int getHeartbeatIntervalMs();
    void setHeartbeatInervalMs(int interval);
    int getBackoffMinMs();
    void setBackoffMinMs(int value);
    int getBackoffMaxMs();
    void setBackoffMaxMs(int value);
    int getRestConccurrencyLevel();
    void setRestConcurrencyLevel(int level);
    void registerMessageHandler(long[] appIds, ClientMessageHandler handler);
    
    //
    // Startup & shutdown
    //
    boolean init(String serviceUri, ClientListener listener);
    void shutdown();
    
    //
    // Login management
    //
    boolean logon(String userIdentifier, String password, String deviceIdentifier);
    void logon(String userIdentifier, String password, String deviceIdentifier, ConsumerCallback<Boolean> responseCallback);
    boolean logonByToken(String loginToken);
    void logonByToken(String loginToken, ConsumerCallback<Boolean> responseCallback);
    void logoff();
    
    long getLoggedUid();
    String getLoginToken();
    
    //
    // REST service
    //
    <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Map<String, String> params, Class<T> responseClz);
    <T extends RestResponseBase> void restCall(String method, String commandRelativeUri, Map<String, String> params, Class<T> responseClz, ConsumerCallback<T> responseCallback);
}
