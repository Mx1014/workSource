// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>count: 剩余数量</li>
 * </ul>
 */
public class GetRemainBroadcastCountResponse {
	private Integer count;

	public GetRemainBroadcastCountResponse() {
		super();
	}

	public GetRemainBroadcastCountResponse(Integer count) {
		super();
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
