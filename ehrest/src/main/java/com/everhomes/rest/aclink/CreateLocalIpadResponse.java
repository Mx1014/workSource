// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>获取ipad列表
 * <li> uuid:</li>
 * </ul>
 */
public class CreateLocalIpadResponse {
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
