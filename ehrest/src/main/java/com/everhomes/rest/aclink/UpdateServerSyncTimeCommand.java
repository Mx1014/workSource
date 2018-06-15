// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serverId:服务器id</li>
 * </ul>
 *
 */
public class UpdateServerSyncTimeCommand {
	private Long serverId;

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
