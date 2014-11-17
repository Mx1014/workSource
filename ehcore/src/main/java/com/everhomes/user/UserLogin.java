package com.everhomes.user;

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
public class UserLogin {
    public enum Status { loggedOff, loggedIn };
    
    private long userId;
    private int loginId;
    private String deviceIdentifier;
    
    private Status status;
    private int loginInstanceNumber;
    private long lastAccessTick;
    
    public UserLogin() {
    }
    
    public UserLogin(long userId, int loginId, String identifier) {
        this.userId = userId;
        this.loginId = loginId;
        this.deviceIdentifier = identifier;
        loginInstanceNumber = new Random().nextInt();
        lastAccessTick = System.currentTimeMillis();
        status = Status.loggedOff;
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
}
