// @formatter:off
package com.everhomes.rest.rpc.client;

import com.everhomes.util.Name;

@Name("register")
public class RegisterConnectionRequestPdu {
    private String loginToken;
    
    public RegisterConnectionRequestPdu() {
    }

    public RegisterConnectionRequestPdu(String loginToken) {
        this.loginToken = loginToken;
    }
    
    public String getLoginToken() {
        return loginToken;
    }
    
    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
