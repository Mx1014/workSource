package com.everhomes.user;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents user login session, compared with other data structure like UserIdentifier or User, it is persisted in
 * memory data store(Redis backed big collection), since device disconnects are the normal in mobile network, a memory
 * data store will help reduce the impact of presence information changes.
 * 
 * A user login session is a long-lasting session until explicit logout is issued. Which means, temporary disconnects
 * from client will not be considered as of being logged out, this will ensure that device push notification messages can
 * be correctly issued
 * 
 * @author Kelven Yang
 *
 */
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 7244772864801329872L;

    public enum Status { loggedOff, loggedIn };
    
    private long userId;
    private int loginId;
    private String deviceIdentifier;
    private int namespaceId;
    
    private Status status;
    private Integer loginBorderId;
    private int loginInstanceNumber;
    private long lastAccessTick;
    
    public UserLogin() {
    }
    
    public UserLogin(int namespaceId, long userId, int loginId, String identifier) {
        this.namespaceId = namespaceId;
        this.userId = userId;
        this.loginId = loginId;
        this.deviceIdentifier = identifier;
        loginInstanceNumber = new Random().nextInt();
        lastAccessTick = System.currentTimeMillis();
        status = Status.loggedOff;
    }
    
    public int getNamespaceId() {
        return this.namespaceId;
    }
    
    public void setNamespaceId(int namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public Integer getLoginBorderId() {
        return this.loginBorderId;
    }
    
    public void setLoginBorderId(Integer loginBorderId) {
        this.loginBorderId = loginBorderId;
    }
    
    public long getUserId() {
        return this.userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public int getLoginId() {
        return this.loginId;
    }
    
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
    
    public void setDeviceIdentifier(String identifier) {
        this.deviceIdentifier = identifier;
    }
    
    public int getLoginInstanceNumber() {
        return loginInstanceNumber;
    }
    
    public void setLoginInstanceNumber(int loginInstanceNumber) {
        this.loginInstanceNumber = loginInstanceNumber;
    }
    
    public long getLastAccessTick() {
        return lastAccessTick;
    }
    
    public void setLastAccessTick(long lastAccessTick) {
        this.lastAccessTick = lastAccessTick;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
        
        if(status == Status.loggedOff) {
            this.loginInstanceNumber++;
        }
    }
    
    public LoginToken getLoginToken() {
        LoginToken token = new LoginToken(this.userId, this.loginId, this.loginInstanceNumber);
        return token;
    }
}
