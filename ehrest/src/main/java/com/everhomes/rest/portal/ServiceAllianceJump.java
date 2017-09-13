// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>name: 跳转名称</li>
 * <li>url: 跳转url</li>
 * </ul>
 */
public class ServiceAllianceJump {

	private String name;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
