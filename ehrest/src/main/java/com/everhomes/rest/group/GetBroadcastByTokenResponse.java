// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>broadcast: 广播消息，参考{@link com.everhomes.rest.group.BroadcastDTO}</li>
 * </ul>
 */
public class GetBroadcastByTokenResponse {

	private BroadcastDTO broadcast;

	public GetBroadcastByTokenResponse() {

	}

	public GetBroadcastByTokenResponse(BroadcastDTO broadcast) {
		super();
		this.broadcast = broadcast;
	}

	public BroadcastDTO getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(BroadcastDTO broadcast) {
		this.broadcast = broadcast;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
