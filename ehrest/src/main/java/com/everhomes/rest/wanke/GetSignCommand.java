package com.everhomes.rest.wanke;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>url: 当前页面url</li>
 * <li>type: 对接方类型</li>
 * </ul>
 */
public class GetSignCommand {
	private String url;
	private String type;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
