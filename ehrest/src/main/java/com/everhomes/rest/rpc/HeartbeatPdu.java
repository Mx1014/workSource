// @formatter:off
package com.everhomes.rest.rpc;

import com.everhomes.util.Name;

@Name("heartbeat")
public class HeartbeatPdu {
    private long lastPeerReceiveTime;
    
    public HeartbeatPdu() {
    }

    public long getLastPeerReceiveTime() {
        return lastPeerReceiveTime;
    }

    public void setLastPeerReceiveTime(long lastPeerReceiveTime) {
        this.lastPeerReceiveTime = lastPeerReceiveTime;
    }
}
