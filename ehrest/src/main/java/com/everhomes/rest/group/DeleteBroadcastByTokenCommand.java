// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>broadcastToken: id的标识</li>
 * </ul>
 */
public class DeleteBroadcastByTokenCommand {

	private String broadcastToken;

	public DeleteBroadcastByTokenCommand() {

	}

	public DeleteBroadcastByTokenCommand(String broadcastToken) {
		super();
		this.broadcastToken = broadcastToken;
	}

	public String getBroadcastToken() {
		return broadcastToken;
	}

	public void setBroadcastToken(String broadcastToken) {
		this.broadcastToken = broadcastToken;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
