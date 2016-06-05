// @formatter:off
package com.everhomes.test.core.base;

import org.springframework.beans.factory.annotation.Value;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.user.LogonCommand;
import com.everhomes.rest.user.LogonRestResponse;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.test.core.http.HttpClientService;
import com.everhomes.test.core.util.EncryptionUtils;
import com.everhomes.util.StringHelper;

public class UserContext {    
    @Value("${app.version:}")
    private String appVersion;

    private transient HttpClientService httpClient;
    
    private VersionRealmType realmType;
    
    private Integer namespaceId;
    
    private LogonRestResponse logonResponse;
    
    /** 当前执行的请求URI */
    private String commandUri;
    
    public UserContext(HttpClientService httpClient) {
        this(httpClient, null);
    }
    
    public UserContext(HttpClientService httpClient, Integer namespaceId) {
        this(httpClient, namespaceId, null);
    }
    
    public UserContext(HttpClientService httpClient, Integer namespaceId, VersionRealmType realmType) {
        this.httpClient = httpClient;
        this.namespaceId = namespaceId;
        this.realmType = realmType;
    }
    
    public boolean logon(Integer namespaceId, String phone, String plainPassword) {
        namespaceId = (namespaceId == null) ? 0 : namespaceId;
        
        LogonCommand cmd = new LogonCommand();
        cmd.setDeviceIdentifier("none");
        cmd.setNamespaceId(namespaceId);
        cmd.setUserIdentifier(phone);
        cmd.setPassword(EncryptionUtils.hashPassword(plainPassword));
        
        return logon(cmd);
    }
    
    public boolean logon(LogonCommand cmd) {
        if(httpClient == null) {
            throw new IllegalStateException("The http client object may not be null when calling http request");
        }
        
        logonResponse = (LogonRestResponse) httpClient.restPost("/user/logon", cmd,
            LogonRestResponse.class);
        if (httpClient.isReponseSuccess(logonResponse)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean logoff() {
        if(httpClient == null) {
            throw new IllegalStateException("The http client object may not be null when calling http request");
        }
        
        if (isLogin()) {
            StringRestResponse response = httpClient.restPost("/user/logoff", null, StringRestResponse.class);
            if (response.getResponse() != null && response.getResponse().equals("OK")) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isLogin() {
        if(this.logonResponse != null) {
            Integer errCode = this.logonResponse.getErrorCode();
            if(errCode != null && (errCode.equals(200) || errCode.equals(201))) {
                return true;
            }
        }
        
        return false;
    }

    public long getLoggedUid() {
        long userId = 0;
        if(this.logonResponse != null && logonResponse.getResponse() != null) {
            userId = logonResponse.getResponse().getUid();
        }
        
        return userId;
    }
    
    public String getLoginToken() {
        String logonToken = null;
        if(this.logonResponse != null && logonResponse.getResponse() != null) {
            logonToken = logonResponse.getResponse().getLoginToken();
        }
        
        return logonToken;
    }
    
    public String getContentServerAddress() {
        String serverAddress = null;
        if(this.logonResponse != null && logonResponse.getResponse() != null) {
            serverAddress = logonResponse.getResponse().getContentServer();
        }
        
        return serverAddress;
    }
    
    public String getUserAgent() {
        StringBuilder sb = new StringBuilder();
        if(realmType != null) {
            sb.append(realmType.getCode());
        } else {
            sb.append("default");
        }
        sb.append("/");
        if(appVersion != null) {
            sb.append(appVersion);
        }
        sb.append(" ");
        sb.append("ns/");
        namespaceId = (namespaceId == null) ? 0 : namespaceId;
        sb.append(namespaceId);
        sb.append(" ");
        // 增加测试标志
        sb.append("env/test");

        return sb.toString();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public VersionRealmType getRealmType() {
        return realmType;
    }

    public void setRealmType(VersionRealmType realmType) {
        this.realmType = realmType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public LogonRestResponse getLogonResponse() {
        return logonResponse;
    }

    public void setLogonResponse(LogonRestResponse logonResponse) {
        this.logonResponse = logonResponse;
    }

    public String getCommandUri() {
        return commandUri;
    }

    public void setCommandUri(String commandUri) {
        this.commandUri = commandUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
