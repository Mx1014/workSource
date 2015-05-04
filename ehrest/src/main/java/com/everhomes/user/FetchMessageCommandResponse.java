// @formatter:off
package com.everhomes.user;

import java.util.List;

import com.everhomes.messaging.MessageDTO;
import com.everhomes.util.StringHelper;

public class FetchMessageCommandResponse {
    private Long nextPageAnchor;
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
