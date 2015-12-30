// @formatter:off
package com.everhomes.rest.rpc.server;

import com.everhomes.util.Name;

/**
 * Ping request message
 * 
 * @author Kelven Yang
 *
 */
@Name("ping.request")
public class PingRequestPdu {
    private long startTick;
    private String body;
    
    public PingRequestPdu() {
        startTick = System.currentTimeMillis();
    }

    public long getStartTick() {
        return this.startTick;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
