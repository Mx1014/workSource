// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>broadcasts: 广播消息列表</li>
 * </ul>
 */
public class ListBroadcastsResponse {

	@ItemType(BroadcastDTO.class)
	private List<BroadcastDTO> broadcasts;

	public ListBroadcastsResponse() {

	}

	public ListBroadcastsResponse(List<BroadcastDTO> broadcasts) {
		super();
		this.broadcasts = broadcasts;
	}

	public List<BroadcastDTO> getBroadcasts() {
		return broadcasts;
	}

	public void setBroadcasts(List<BroadcastDTO> broadcasts) {
		this.broadcasts = broadcasts;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
