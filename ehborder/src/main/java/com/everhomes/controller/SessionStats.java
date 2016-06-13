// @formatter:off
package com.everhomes.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;

public class SessionStats {
    private AtomicLong lastPeerReceiveTick = new AtomicLong();
    private AtomicLong lastSendTick = new AtomicLong();
    
    public SessionStats() {
    }

    public long getLastPeerReceiveTick() {
        return lastPeerReceiveTick.get();
    }

    public void setLastPeerReceiveTick(long lastPeerReceiveTick) {
        this.lastPeerReceiveTick.set(lastPeerReceiveTick);
    }

    public long getLastSendTick() {
        return lastSendTick.get();
    }

    public void setLastSendTick(long lastSendTick) {
        this.lastSendTick.set(lastSendTick);
    }
    
    public void updatePeerReceiveTick() {
        this.lastPeerReceiveTick.set(DateHelper.currentGMTTime().getTime());
    }
    
    public void updateSendTick() {
        this.lastSendTick.set(DateHelper.currentGMTTime().getTime());
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
