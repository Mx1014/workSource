// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>broadcastToken: id的标识</li>
 * </ul>
 */
public class GetBroadcastByTokenCommand {

	private String broadcastToken;

	public GetBroadcastByTokenCommand() {

	}

	public GetBroadcastByTokenCommand(String broadcastToken) {
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
