package com.everhomes.user;

import java.util.Random;

public class DeviceLogin {
    private UserDevice loginDevice;
    
    private int loginInstanceNumber;
    private long lastAccessTick;
    
    public DeviceLogin(UserDevice loginDevice) {
        this.loginDevice = loginDevice;
        loginInstanceNumber = new Random().nextInt();
        lastAccessTick = System.currentTimeMillis();
    }
    
    public UserDevice getLoginDevice() {
        return loginDevice;
    }
    
    public void setLoginDevice(UserDevice loginDevice) {
        this.loginDevice = loginDevice;
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
}
