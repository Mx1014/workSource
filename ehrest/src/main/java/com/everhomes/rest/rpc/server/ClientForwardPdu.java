// @formatter:off
package com.everhomes.rest.rpc.server;

import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.util.Name;

@Name("client.forward")
public class ClientForwardPdu {
    private String loginToken;
    private String frame;
    
    public ClientForwardPdu() {
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public void setClientFrame(PduFrame frame) {
        this.frame = frame.toJson();
    }
    
    public PduFrame getClientFrame() {
        return PduFrame.fromJson(this.frame);
    }
    
    public String getEncodedFrame() {
        return this.frame;
    }
}
