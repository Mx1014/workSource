// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：服务器id</li>
 * </ul>
 */
public class SyncLocalServerCommand {
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
