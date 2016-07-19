// @formatter:off
package com.everhomes.rest.rpc.server;

import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;

/**
 * 
 * Ping response message
 * 
 * @author Kelven Yang
 *
 */
@Name("ping.response")
public class PingResponsePdu {
    private long startTick;
    private String body;

    public PingResponsePdu(PingRequestPdu request) {
        this.startTick = request.getStartTick();
        this.body = request.getBody();
    }
    
    public long getStartTick() {
        return this.startTick;
    }
    
    public String getBody() {
        return this.body;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
