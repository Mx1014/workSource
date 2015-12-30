// @formatter:off
package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页游标位置</li>
 * <li>messages: 消息列表。参考 {@link com.everhomes.rest.messaging.MessageDTO} </li>
 * </ul>
 *
 */
public class FetchMessageCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(MessageDTO.class)
    List<MessageDTO> messages;
    
    public FetchMessageCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
