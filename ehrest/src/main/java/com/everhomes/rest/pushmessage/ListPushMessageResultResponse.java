package com.everhomes.rest.pushmessage;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListPushMessageResultResponse {
    private Long nextPageAnchor;

    @ItemType(PushMessageResultDTO.class)
    private List<PushMessageResultDTO> pushMessages;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PushMessageResultDTO> getPushMessages() {
        return pushMessages;
    }

    public void setPushMessages(List<PushMessageResultDTO> pushMessages) {
        this.pushMessages = pushMessages;
    }
    
    

}
