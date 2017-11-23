package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 调用JS接口页面的完整URL</li>
 * </ul>
 */
public class GetSignatureCommand {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
