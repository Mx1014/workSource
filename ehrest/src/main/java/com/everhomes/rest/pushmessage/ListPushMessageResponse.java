package com.everhomes.rest.pushmessage;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListPushMessageResponse {
    private Long nextPageAnchor;

    @ItemType(PushMessageDTO.class)
    private List<PushMessageDTO> pushMessages;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PushMessageDTO> getPushMessages() {
        return pushMessages;
    }

    public void setPushMessages(List<PushMessageDTO> pushMessages) {
        this.pushMessages = pushMessages;
    }
}
