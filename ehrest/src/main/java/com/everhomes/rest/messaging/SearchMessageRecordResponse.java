package com.everhomes.rest.messaging;

import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.util.StringHelper;

import java.util.List;

public class SearchMessageRecordResponse {
    private List<MessageRecordDto> dtos;
    private Long nextPageAnchor;

    public List<MessageRecordDto> getDtos() {
        return dtos;
    }

    public void setDtos(List<MessageRecordDto> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
