// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>messageSenderDTOs: 消息推送者列表，参考{@link com.everhomes.rest.talent.MessageSenderDTO}</li>
 * </ul>
 */
public class ListMessageSenderResponse {
	@ItemType(MessageSenderDTO.class)
	private List<MessageSenderDTO> messageSenderDTOs;

	public List<MessageSenderDTO> getMessageSenderDTOs() {
		return messageSenderDTOs;
	}

	public void setMessageSenderDTOs(List<MessageSenderDTO> messageSenderDTOs) {
		this.messageSenderDTOs = messageSenderDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
