// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>生成配对码
 * <li> uuid: 配对码</li>
 * </ul>
 *
 */
public class CreateMarchUUIDResponse {
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
