// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>broadcasts: 广播消息列表 {@link com.everhomes.rest.group.BroadcastDTO}</li>
 *     <li>nextPageAnchor: 下页锚点</li>
 *     <li>count: count</li>
 * </ul>
 */
public class ListBroadcastsResponse {

	@ItemType(BroadcastDTO.class)
	private List<BroadcastDTO> broadcasts;

	private Long nextPageAnchor;

	private Integer count;

	public ListBroadcastsResponse() {

	}

	public ListBroadcastsResponse(List<BroadcastDTO> broadcasts, Long nextPageAnchor, Integer count) {
		super();
		this.broadcasts = broadcasts;
		this.nextPageAnchor = nextPageAnchor;
		this.count = count;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<BroadcastDTO> getBroadcasts() {
		return broadcasts;
	}

	public void setBroadcasts(List<BroadcastDTO> broadcasts) {
		this.broadcasts = broadcasts;
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
