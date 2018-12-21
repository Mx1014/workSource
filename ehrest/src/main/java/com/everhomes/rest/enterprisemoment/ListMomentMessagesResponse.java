// @formatter:off
package com.everhomes.rest.enterprisemoment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>momentMessages: 消息列表{@link com.everhomes.rest.enterprisemoment.MomentMessageDTO}</li>
 * </ul>
 */
public class ListMomentMessagesResponse {
	private Long nextPageAnchor;
	private List<MomentMessageDTO> momentMessages;

	public ListMomentMessagesResponse() {

	}

	public ListMomentMessagesResponse(Long nextPageAnchor, List<MomentMessageDTO> momentMessages) {
		this.nextPageAnchor = nextPageAnchor;
		this.momentMessages = momentMessages;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<MomentMessageDTO> getMomentMessages() {
		return momentMessages;
	}

	public void setMomentMessages(List<MomentMessageDTO> momentMessages) {
		this.momentMessages = momentMessages;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
