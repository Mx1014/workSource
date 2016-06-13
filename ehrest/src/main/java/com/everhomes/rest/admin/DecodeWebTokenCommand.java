package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>webToken: web token</li>
 * <li>tokenType: token 类型</li>
 * </ul>
 */
public class DecodeWebTokenCommand {
	private String webToken;
	
    private String tokenType;

    public String getWebToken() {
        return webToken;
    }

    public void setWebToken(String webToken) {
        this.webToken = webToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
