package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>json: 要转成webtoken的参数json</li>
 * <li>tokenType: token 类型</li>
 * </ul>
 */
public class EncodeWebTokenCommand {
	
	private String tokenType;
	
	private String json;
	
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
