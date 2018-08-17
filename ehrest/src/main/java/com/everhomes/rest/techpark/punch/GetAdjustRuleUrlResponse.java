package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>url:跳转链接</li>
 * </ul>
 */
public class GetAdjustRuleUrlResponse { 
	private String url;
	public GetAdjustRuleUrlResponse(){}
	public GetAdjustRuleUrlResponse(String url){
		super();
		this.url = url;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}  
}
