// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId:用户id</li>
 * <li>serverId:服务器id</li>
 * </ul>
 *
 */
public class SyncLocalUserDataCommand {
	private Long userId;
	private Long serverId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
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
